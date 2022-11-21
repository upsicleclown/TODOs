package cache

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.encodeToJsonElement
import models.WindowSettings
import java.io.File

/**
 * Class to cache items for the frontend.
 * Cache files path must be at root for packaging to work.
*/
class Cache(private val windowSettingsFilePath: String = "window_settings.json", private val groupIdToItemIdOrderingFilePath: String = "group_id_to_item_id_ordering.json") {
    private val emptyLength = 0L

    /* Properties related to window settings. */
    private lateinit var windowSettings: WindowSettings
    private val defaultPosition = 0.0
    private val defaultSize = 500.0

    /* Properties related to item ordering. */
    private var groupIdToItemIdOrdering: HashMap<Int, List<Int>> = HashMap()

    init {
        // If file content is empty, set default values.
        if (File(windowSettingsFilePath).length() == emptyLength) {
            windowSettings = WindowSettings(defaultPosition, defaultPosition, defaultSize, defaultSize)
        } else {
            loadWindowSettings()
        }

        // If file content is non-empty, load values.
        if (File(groupIdToItemIdOrderingFilePath).length() != emptyLength) {
            loadGroupIdToItemIdOrdering()
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

    /* Methods related to item ordering */
    fun editGroupToItemOrdering(groupIdToItemIdOrdering: HashMap<Int, List<Int>>) {
        this.groupIdToItemIdOrdering = groupIdToItemIdOrdering
    }

    fun getGroupIdToItemIdOrdering(): HashMap<Int, List<Int>> {
        return groupIdToItemIdOrdering
    }

    fun saveGroupIdToItemIdOrdering() {
        val jsonDict: JsonElement = Json.encodeToJsonElement(groupIdToItemIdOrdering)
        File(groupIdToItemIdOrderingFilePath).writeText(jsonDict.toString())
    }

    private fun loadGroupIdToItemIdOrdering() {
        val jsonDict = File(groupIdToItemIdOrderingFilePath).readText()
        groupIdToItemIdOrdering = Json.decodeFromString(jsonDict)
    }
}
