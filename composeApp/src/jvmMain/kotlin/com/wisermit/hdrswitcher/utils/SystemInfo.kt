package com.wisermit.hdrswitcher.utils

object SystemInfo {
    val userHomePath: String get() = System.getProperty("user.home")
    val systemDrive: String get() = System.getenv("SystemDrive")
}