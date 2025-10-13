package com.wisermit.hdrswitcher.system.process

import com.wisermit.hdrswitcher.util.Log
import java.awt.Robot
import java.awt.event.KeyEvent

private val TAG = SystemManagerPowerShell::class.java.simpleName

private const val MONITOR_DATA_STORE_REG_PATH =
    """HKLM:\SYSTEM\CurrentControlSet\Control\GraphicsDrivers\MonitorDataStore"""
private const val HDR_ENABLED_REG_KEY = "HDREnabled"
private const val DATA_SEPARATOR = ","

object SystemManagerPowerShell {

    fun getHdrStatus(): Boolean? = getHdrMonitors()
        .takeIf { it.isNotEmpty() }
        ?.any { it.hdrEnabled }

    private fun getHdrMonitors(): List<MonitorData> =
        PowerShell
            .execute(Command.GET_ACTIVE_MONITORS)
            .also { Log.i(TAG, "active monitors: $it") }
            .flatMap { monitorId ->
                PowerShell
                    .execute(Command.getHdrMonitorData(monitorId))
                    .map { monitorDataString ->
                        monitorDataString.split(DATA_SEPARATOR).let {
                            MonitorData(
                                regKey = it[0],
                                hdrEnabled = it[1] == "1"
                            )
                        }
                    }
            }
            .also { Log.i(TAG, "hdr monitors: $it") }

    fun toggleHdr() {
        listOf(
            KeyEvent.VK_WINDOWS,
            KeyEvent.VK_ALT,
            KeyEvent.VK_B,
        ).run {
            with(Robot()) {
                forEach(::keyPress)
                forEach(::keyRelease)
            }
        }
    }
}

private object Command {

    const val GET_ACTIVE_MONITORS =
        "Get-CimInstance -Namespace root\\wmi -ClassName WmiMonitorBasicDisplayParams | " +
                "Where-Object { \$_.Active } | " +
                "Select-Object -ExpandProperty InstanceName | " +
                "ForEach-Object { (\$_ -split '\\\\')[1] }"

    fun getHdrMonitorData(deviceId: String) =
        "Get-ChildItem \"$MONITOR_DATA_STORE_REG_PATH\" |\n" +
                "Where-Object {\n" +
                "    \$_.PSChildName -like \\\"$deviceId*\\\" -and\n" +
                "    (Get-ItemProperty -Path \$_.PsPath -Name \"$HDR_ENABLED_REG_KEY\" -ErrorAction SilentlyContinue)\n" +
                "} |\n" +
                "ForEach-Object {\n" +
                "    \$monitorRegKey = \$_.PSChildName\n" +
                "    \$hdrEnabled = (Get-ItemProperty -Path \$_.PsPath -Name \"$HDR_ENABLED_REG_KEY\").$HDR_ENABLED_REG_KEY\n" +
                "    \\\"\$monitorRegKey$DATA_SEPARATOR\$hdrEnabled\\\"\n" +
                "}"
}

private data class MonitorData(
    val regKey: String,
    val hdrEnabled: Boolean,
)