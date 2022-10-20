package cache

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.encodeToJsonElement
import models.WindowSettings
import java.io.File

/*
Class to cache items for the frontend.
The window settings file path could be a secret.
*/
class Cache(private val windowSettingsFilePath: String = "src/main/kotlin/cache/window_settings.json") {

    /* Properties related to window settings. */
    private lateinit var windowSettings: WindowSettings

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
        File(windowSettingsFilePath).writeText(jsonDict.toString())
    }

    private fun loadWindowSettings() {
        val jsonDict = File(windowSettingsFilePath).readText()
        windowSettings = Json.decodeFromString(jsonDict)
    }
}
