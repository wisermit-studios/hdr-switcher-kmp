package com.wisermit.hdrswitcher.ui.theme

import com.sun.jna.platform.win32.Advapi32Util
import com.sun.jna.platform.win32.WinReg

private const val REG_THEME_PATH =
    "Software\\Microsoft\\Windows\\CurrentVersion\\Themes\\Personalize"
private const val REG_IS_LIGHT_THEME_KEY = "AppsUseLightTheme"

fun isSystemInDarkTheme2(): Boolean {
    val isDark = Advapi32Util.registryValueExists(
        WinReg.HKEY_CURRENT_USER,
        REG_THEME_PATH,
        REG_IS_LIGHT_THEME_KEY
    ) && Advapi32Util.registryGetIntValue(
        WinReg.HKEY_CURRENT_USER,
        REG_THEME_PATH,
        REG_IS_LIGHT_THEME_KEY
    ) == 0

    println("## isDark: $isDark")

    return isDark
}
