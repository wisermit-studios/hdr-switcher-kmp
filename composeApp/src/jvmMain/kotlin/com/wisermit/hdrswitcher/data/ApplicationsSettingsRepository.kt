package com.wisermit.hdrswitcher.data

import com.wisermit.hdrswitcher.AppConfig
import com.wisermit.hdrswitcher.model.ApplicationSettings
import java.io.File
import java.net.URI
import kotlin.io.path.toPath

class ApplicationsSettingsRepository {

    private val cache = mutableListOf<ApplicationSettings>()

    private fun getFile(): File = File(AppConfig.APPLICATIONS_SETTINGS_PATH)

    fun fetch(): List<ApplicationSettings> {
        return runCatching {
            cache.clear()
            val items = getFile().readLines().map {
                // TODO: Display error.
                ApplicationSettings(it.split("|"))
            }
            cache.addAll(items)
            cache
        }.getOrElse {
            println(it)
            cache
        }
    }

    fun save(appUri: URI): Result<Unit> {
        // TODO: Check file type.
        // TODO: Display error.
        return save(ApplicationSettings(appUri.toPath()))
    }

    fun save(appSettings: ApplicationSettings): Result<Unit> {
        cache.removeAll { it.path == appSettings.path }
        cache.add(appSettings)
        cache.sortBy { it.name }

        runCatching {
            var file = getFile()
            if (!file.exists()) {
                file.parentFile.mkdirs()
                file = getFile()
            }
            file.bufferedWriter().use { writer ->
                cache.forEach {
                    writer.write(it.toLine() + "\n")
                }
            }
        }.onFailure {
            println(it)
            return Result.failure(it)
        }
        return Result.success(Unit)
    }
}