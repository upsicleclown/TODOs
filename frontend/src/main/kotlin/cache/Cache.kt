package cache

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.encodeToJsonElement
import models.WindowSettings
import java.io.File

/**
 * Class to cache items for the frontend.
 * The window settings file path must be at root for packaging to work.
*/
class Cache(private val windowSettingsFilePath: String = "window_settings.json") {
    /* Properties related to window settings. */
    private lateinit var windowSettings: WindowSettings
    private val emptyLength = 0L
    private val defaultPosition = 0.0
    private val defaultSize = 500.0

    init {
        // If file content is empty, set default values.
        if (File(windowSettingsFilePath).length() == emptyLength) {
            windowSettings = WindowSettings(defaultPosition, defaultPosition, defaultSize, defaultSize)
        } else {
            loadWindowSettings()
        }
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
