package ui

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.encodeToJsonElement
import models.WindowSettings
import java.io.File

class Cache {

    /* Properties related to window settings. */
    private lateinit var windowSettings: WindowSettings

    // Window settings will remain saved in file. File path could potentially become a secret.
    private val windowSettingFilePath = this.javaClass.classLoader.getResource("window_settings.json")!!.path

    init {
        loadWindowSettings()
    }

    /* Methods related to window settings */
    fun editWindowSettings(windowSettings: WindowSettings) {
        this.windowSettings = windowSettings
    }

    fun getWindowSettings(): WindowSettings {
        return windowSettings
    }

    fun saveWindowSettings() {
        val jsonDict: JsonElement = Json.encodeToJsonElement(windowSettings)
        File(windowSettingFilePath).writeText(jsonDict.toString())
    }

    private fun loadWindowSettings() {
        val jsonDict = File(windowSettingFilePath).readText()
        windowSettings = Json.decodeFromString(jsonDict)
    }
}
