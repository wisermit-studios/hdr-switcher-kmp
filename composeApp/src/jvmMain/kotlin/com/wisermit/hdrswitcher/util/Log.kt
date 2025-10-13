package com.wisermit.hdrswitcher.util

import java.io.PrintWriter
import java.text.SimpleDateFormat

private val DATE_FORMAT = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")

object Log {

    private const val TAG_LENGTH = 28

    var level = Level.None

    fun d(tag: String, msg: String) {
        log(Level.Debug, tag, msg)
    }

    fun i(tag: String, msg: String) {
        log(Level.Info, tag, msg)
    }

    fun w(tag: String, msg: String) {
        log(Level.Warning, tag, msg)
    }

    fun e(tag: String, msg: String, throwable: Throwable? = null) {
        log(Level.Error, tag, msg, throwable)
    }

    @Deprecated("Remove before commit.")
    fun test(msg: String) {
        log(Level.Test, "", "... $msg")
    }

    private fun log(level: Level, tag: String, msg: String, tr: Throwable? = null) {
        if (level <= Log.level) {
            val timestamp = DATE_FORMAT.format(System.currentTimeMillis())
            val tag = tag.padEnd(TAG_LENGTH, ' ')
            val priority = level.name[0]

            val pw = PrintWriter(System.out, true)
            pw.println("$timestamp, $tag,$priority, $msg")
            tr?.printStackTrace(pw)
        }
    }

    enum class Level(val value: Int) {
        None(0), Error(3), Warning(4), Info(6), Debug(7), Test(8),
    }
}