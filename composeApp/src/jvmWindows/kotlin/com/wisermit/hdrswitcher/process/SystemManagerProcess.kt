package com.wisermit.hdrswitcher.process

import com.wisermit.hdrswitcher.util.AppResources
import com.wisermit.hdrswitcher.util.Log
import com.wisermit.hdrswitcher.util.systemManagerExe
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
private const val END_LOG_MESSAGE_DELIMITER = '\u00A0'

private val RESULT_HEX_FORMAT = HexFormat {
    upperCase = true
    number.prefix = "0x"
}

class SystemManagerProcess private constructor(
    coroutineContext: CoroutineContext,
    private val process: Process,
    private var onExit: OnExitListener? = null
) {
    val coroutineHandler = CoroutineExceptionHandler { _, e ->
        Log.e(TAG, "Job failure.", e)
    }
    private val processJob = SupervisorJob()
    private val processScope = CoroutineScope(coroutineContext + coroutineHandler + processJob)
    private val readersJob: Job

    private var outputWriter: BufferedWriter
    private var resultList = mutableListOf<String>()

    init {
        process.run {
            onExit()?.thenAccept { exit() }
            outputWriter = outputWriter()

            readersJob = processScope.launch(Dispatchers.IO) {
                launch {
                    // TODO: Read blocks.
                    inputReader(Charsets.UTF_8)?.forEachLine { line ->
                        val messages = line.split(END_LOG_MESSAGE_DELIMITER)
                        messages.forEach {
                            readOutputLine(it.trim('\n'))
                        }
                    }
                }
                launch {
                    errorReader().useLines {
                        readOutputLine(it.joinToString("\n"))
                    }
                }
            }
        }
    }

    fun sendCommand(vararg commands: String) {
        outputWriter.run {
            commands.forEach { write(it) }
            write(System.lineSeparator())
            flush()
        }
    }

    fun destroy(): Job {
        process.destroy()
        return processJob
    }

    suspend fun await() = processJob.join()

    private fun exit() {
        processScope.launch {
            readersJob.join()

            val exitCode = process.exitValue()

            process.runCatching {
                inputStream.close()
                errorStream.close()
                outputWriter.close()
            }

            onExit?.invoke(Result(exitCode, resultList))
            processJob.cancel()
        }
    }

    private fun readOutputLine(line: String) {
        if (line.isEmpty()) return

        val linePrefix = line.take(LINE_PREFIX_LENGTH)
        val lineContent = line.drop(LINE_PREFIX_LENGTH)

        when (linePrefix) {
            "R=" -> resultList.add(lineContent)
            "D/" -> Log.d(TAG_EXE, lineContent)
            "I/" -> Log.i(TAG_EXE, lineContent)
            "W/" -> Log.w(TAG_EXE, lineContent)
            "E/" -> Log.e(TAG_EXE, lineContent)
            else -> Log.e(TAG_EXE, line)
        }
    }

    companion object {
        private const val LINE_PREFIX_LENGTH = 2

        suspend fun start(init: Builder.() -> Unit) = Builder().also(init).start()
    }

    data class Result(
        val code: Int,
        val values: List<String>,
    ) {
        val hexCode: String = code.toHexString(RESULT_HEX_FORMAT)
    }

    class Builder {
        var data: String = ""
        var onExit: OnExitListener? = null

        suspend fun start(): SystemManagerProcess {
            val verbosity = when (Log.level) {
                Log.Level.None -> "quiet"
                Log.Level.Error -> "minimal"
                Log.Level.Warning -> "normal"
                Log.Level.Info -> "detailed"
                Log.Level.Debug,
                Log.Level.Test -> "diagnostic"
            }

            val command = arrayOf(
                AppResources.systemManagerExe.path,
                "service", "start",
                "--verbosity", verbosity,
                "--data", data,
            )

            val process = ProcessBuilder(*command).start()
            return SystemManagerProcess(coroutineContext, process, onExit)
        }
    }
}