package com.wisermit.hdrswitcher.system.process

import com.wisermit.hdrswitcher.AppResources
import com.wisermit.hdrswitcher.util.Log
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.io.BufferedWriter
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.coroutineContext

typealias OnExitListener = (SystemManagerProcess.Result) -> Unit

private val TAG = SystemManagerProcess::class.java.simpleName
private val TAG_EXE = "${TAG}_Exe"

class SystemManagerProcess private constructor(
    coroutineContext: CoroutineContext,
    private val process: Process,
    private var onExit: OnExitListener? = null
) {
    val coroutineHandler = CoroutineExceptionHandler { _, e ->
        Log.e(TAG, "Job failure.", e)
    }
    private val processScope = CoroutineScope(coroutineContext + coroutineHandler + SupervisorJob())
    private val readersJob: Job

    private var outputWriter: BufferedWriter
    private var resultList = mutableListOf<String>()

    init {
        process.run {
            onExit()?.thenAccept { exit() }
            outputWriter = outputWriter()

            readersJob = processScope.launch(Dispatchers.IO) {
                launch {
                    inputReader()?.forEachLine {
                        handleInputLine(it)
                    }
                }
                launch {
                    errorReader().useLines {
                        Log.e(TAG_EXE, it.joinToString("\n"))
                    }
                }
            }
        }
    }

    fun command(vararg commands: String) {
        outputWriter.run {
            commands.forEach { write(it) }
            write(System.lineSeparator())
            flush()
        }
    }

    fun destroy() = process.destroy()

    private fun exit() {
        processScope.launch {
            readersJob.join()

            val exitCode = process.exitValue()
            Log.d(TAG, "Process exited: ${exitCode.toHexString(HEX_FORMAT)}")

            process.runCatching {
                inputStream.close()
                errorStream.close()
                outputWriter.close()
            }

            onExit?.invoke(Result(exitCode, resultList))
        }
    }

    private fun handleInputLine(line: String) {
        val linePrefix = line.take(LINE_PREFIX_LENGTH)
        val lineContent = line.drop(LINE_PREFIX_LENGTH)

        when (linePrefix) {
            "R=" -> resultList.add(lineContent)
            "D/" -> Log.d(TAG_EXE, lineContent)
            "I/" -> Log.i(TAG_EXE, lineContent)
            "W/" -> Log.w(TAG_EXE, lineContent)
            "E/" -> Log.e(TAG_EXE, lineContent)
            else -> Log.e(TAG_EXE, "Unknown input line: \"$line\"")
        }
    }

    companion object {
        private const val LINE_PREFIX_LENGTH = 2

        private val HEX_FORMAT = HexFormat {
            upperCase = true
            number.prefix = "0x"
        }

        suspend fun start(init: Builder.() -> Unit) = Builder().also(init).start()
    }

    data class Result(
        val code: Int,
        val values: List<String>,
    )

    class Builder {

        var args: Array<out String> = emptyArray()
            private set

        var onExit: OnExitListener? = null

        fun setArgs(vararg args: String) {
            this.args = args
        }

        suspend fun start(): SystemManagerProcess {
            val exeFile = AppResources.systemManagerExe
            val process = ProcessBuilder(exeFile.path, *args).start()
            return SystemManagerProcess(coroutineContext, process, onExit)
        }
    }
}