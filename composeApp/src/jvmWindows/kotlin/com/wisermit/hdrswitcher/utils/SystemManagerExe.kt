package com.wisermit.hdrswitcher.utils

import com.wisermit.hdrswitcher.AppResources
import com.wisermit.hdrswitcher.framework.Log
import com.wisermit.hdrswitcher.framework.ProcessException

class SystemManagerExe {

    @Throws(ProcessException::class)
    fun start() {
        val exe = AppResources.systemManagerExe.toString()

        return ProcessBuilder(exe).start().run {
            inputStream.bufferedReader().forEachLine {
                Log.test("## stream: $it")
            }

            errorStream.bufferedReader().forEachLine {
                Log.test("## error: $it")
            }

            val exitCode = waitFor()
            destroy()

            Log.test("## exit $exitCode")
        }
    }
}