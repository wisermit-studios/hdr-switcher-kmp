package com.wisermit.hdrswitcher.framework

import java.io.PrintWriter

object Log {

    private const val TAG_LENGTH = 28
    private const val PREFIX = "##"

    private val isEnabled = true

    fun d(tag: String, msg: String) {
        log(Priority.Debug, tag, msg)
    }

    fun i(tag: String, msg: String) {
        log(Priority.Info, tag, msg)
    }

    fun e(tag: String, msg: String, throwable: Throwable? = null) {
        log(Priority.Error, tag, msg, throwable)
    }

    @Deprecated("Remove before commit.")
    fun test(msg: String) {
        log(Priority.Test, "", msg)
    }

    private fun log(priority: Priority, tag: String, msg: String, tr: Throwable? = null) {
        if (isEnabled) {
            val pw = PrintWriter(System.out, true)
            val tag = tag.padEnd(TAG_LENGTH, ' ')
            val priority = priority.name[0]
            pw.println("$PREFIX,$tag,$priority,$msg")
            tr?.printStackTrace(pw)
        }
    }

    private enum class Priority(id: Int) {
        Test(0), Debug(3), Info(4), Error(6)
    }
}