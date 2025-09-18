package com.wisermit.hdrswitcher.infrastructure.systemmanager

import com.wisermit.hdrswitcher.framework.Log
import com.wisermit.hdrswitcher.infrastructure.InputSimulator
import com.wisermit.hdrswitcher.infrastructure.PowerShell
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.awt.event.KeyEvent
import java.io.File

private val TAG = WindowsSystemManager::class.java.simpleName

private const val MONITOR_DATA_STORE_REG_PATH =
    """HKLM:\SYSTEM\CurrentControlSet\Control\GraphicsDrivers\MonitorDataStore"""
private const val HDR_ENABLED_REG_KEY = "HDREnabled"
private const val DATA_SEPARATOR = ","

internal class WindowsSystemManager : SystemManager {

    private val hdrStatus = MutableStateFlow<Boolean?>(null)

    override fun getHdrStatus(): Flow<Boolean?> =
        hdrStatus.asStateFlow()

    override fun refreshHdrStatus() {
        getHdrMonitors()
            .takeIf { it.isNotEmpty() }
            ?.any { it.hdrEnabled }
            .let(hdrStatus::tryEmit)
    }

    override fun toggleHdr() =
        InputSimulator.sendKeyEvent(
            KeyEvent.VK_WINDOWS,
            KeyEvent.VK_ALT,
            KeyEvent.VK_B,
        )

    private fun getHdrMonitors(): List<MonitorData> =
        PowerShell
            .exec(Command.GET_ACTIVE_MONITORS)
            .also { Log.i(TAG, "active monitors: $it") }
            .flatMap { monitorId ->
                PowerShell
                    .exec(Command.getHdrMonitorData(monitorId))
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

    // FIXME: Fix charset (™).
    override suspend fun getFileDescription(file: File): String? =
        PowerShell
            .exec(Command.getFileDescription(file))
            .firstOrNull()
}

private object Command {

    const val GET_ACTIVE_MONITORS =
        "Get-CimInstance -Namespace root\\wmi -ClassName WmiMonitorBasicDisplayParams | " +
                "Where-Object { \$_.Active } | " +
                "Select-Object -ExpandProperty InstanceName | " +
                "ForEach-Object { (\$_ -split '\\\\')[1] }"

    fun getFileDescription(file: File) = "(Get-Item '$file').VersionInfo.FileDescription"

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
