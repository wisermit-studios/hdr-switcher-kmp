package com.wisermit.hdrswitcher.infrastructure

import com.wisermit.hdrswitcher.utils.InputSimulator
import com.wisermit.hdrswitcher.utils.PowerShell
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.awt.event.KeyEvent
import java.io.File

private const val MONITOR_DATA_STORE_REG_PATH =
    """HKLM:\SYSTEM\ControlSet001\Control\GraphicsDrivers\MonitorDataStore"""

private const val HDR_ENABLED_REG_KEY = "HDREnabled"

internal class WindowsSystemManager : SystemManager {

    private val isHdrEnabled = MutableStateFlow<Boolean?>(null)

    override fun isHdrEnabled(): StateFlow<Boolean?> {
        CoroutineScope(Dispatchers.IO).launch {
            refreshHdrStatus()
        }
        return isHdrEnabled.asStateFlow()
    }

    private suspend fun refreshHdrStatus() {
        val hdrMonitors = getHdrMonitors()
        val hdrStatus = when {
            hdrMonitors.isEmpty() -> null
            else -> hdrMonitors.any { it.hdrEnabled }
        }
        isHdrEnabled.tryEmit(hdrStatus)
    }

    private suspend fun getHdrMonitors(): List<MonitorData> {
        val activeMonitorIdList = PowerShell.exec(Command.GET_ACTIVE_MONITORS)
            .getOrNull()
            ?.filterNotNull()

        println("## active monitors: $activeMonitorIdList")

        val hdrMonitors = activeMonitorIdList?.mapNotNull { monitorId ->
            PowerShell.exec(Command.getHdrMonitorData(monitorId))
                .getOrNull()
                ?.mapNotNull { monitorDataLine ->
                    monitorDataLine?.split(",")?.let {
                        MonitorData(
                            regKey = it[0],
                            hdrEnabled = it[1] == "1"
                        )
                    }
                }
        }?.flatten()

        println("## hdr monitors: $hdrMonitors")

        return hdrMonitors ?: emptyList()
    }

    override suspend fun setSystemHdr(enabled: Boolean) {
        refreshHdrStatus()
        if (isHdrEnabled.value != enabled) {
            runCatching {
                InputSimulator.sendKeyEvent(
                    KeyEvent.VK_WINDOWS,
                    KeyEvent.VK_ALT,
                    KeyEvent.VK_B,
                )
            }
            refreshHdrStatus()
        }
    }

    // FIXME: Fix charset (™).
    override suspend fun getFileDescription(file: File): String? {
        return PowerShell.exec(Command.getFileDescription(file))
            .getOrNull()
            ?.firstOrNull()
    }
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
                "    \\\"\$monitorRegKey,\$hdrEnabled\\\"\n" +
                "}"
}

private data class MonitorData(
    val regKey: String,
    val hdrEnabled: Boolean,
)
