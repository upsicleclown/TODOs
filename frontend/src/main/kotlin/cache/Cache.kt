package cache

import javafx.beans.property.SimpleBooleanProperty
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.encodeToJsonElement
import models.WindowSettings
import theme.Theme
import java.io.File

/**
 * Class to cache items for the frontend.
 * Cache files path must be at root for packaging to work.
*/
class Cache(
    private val windowSettingsFilePath: String = "window_settings.json",
    private val groupToItemOrderingFilePath: String = "group_id_to_item_id_ordering.json",
    private val userToItemOrderingFilePath: String = "user_id_to_item_id_ordering.json"
) {

    private val emptyLength = 0L

    /* Exposed so views can listen for live theme updates and repaint */
    var themeChangeProperty = SimpleBooleanProperty()

    /* Properties related to window settings. */
    private lateinit var windowSettings: WindowSettings
    private val defaultTheme = Theme.LIGHT
    private val defaultPosition = 0.0
    private val defaultSize = 500.0

    /* Properties related to item ordering. */
    private var groupToItemOrdering: HashMap<Int, List<Int>> = HashMap()
    private var userToItemOrdering: HashMap<Int, List<Int>> = HashMap()

    init {
        // If file content is empty, set default values.
        if (File(windowSettingsFilePath).length() == emptyLength) {
            windowSettings = WindowSettings(defaultPosition, defaultPosition, defaultSize, defaultSize, defaultTheme)
        } else {
            loadWindowSettings()
        }

        // If file content is non-empty, load values.
        if (File(groupToItemOrderingFilePath).length() != emptyLength) {
            loadGroupToItemOrdering()
        }

        // If file content is non-empty, load values.
        if (File(userToItemOrderingFilePath).length() != emptyLength) {
            loadUserToItemOrdering()
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

    /* Methods related to group item ordering */
    fun editGroupToItemOrdering(groupIdToItemIdOrdering: HashMap<Int, List<Int>>) {
        this.groupToItemOrdering = groupIdToItemIdOrdering
    }

    fun getGroupToItemOrdering(): HashMap<Int, List<Int>> {
        return groupToItemOrdering
    }

    fun saveGroupToItemOrdering() {
        val jsonDict: JsonElement = Json.encodeToJsonElement(groupToItemOrdering)
        File(groupToItemOrderingFilePath).writeText(jsonDict.toString())
    }

    private fun loadGroupToItemOrdering() {
        val jsonDict = File(groupToItemOrderingFilePath).readText()
        groupToItemOrdering = Json.decodeFromString(jsonDict)
    }

    /* Methods related to user item ordering */
    fun editUserToItemOrdering(userToItemOrdering: HashMap<Int, List<Int>>) {
        this.userToItemOrdering = userToItemOrdering
    }

    fun getUserToItemOrdering(): HashMap<Int, List<Int>> {
        return userToItemOrdering
    }

    fun saveUserToItemOrdering() {
        val jsonDict: JsonElement = Json.encodeToJsonElement(userToItemOrdering)
        File(userToItemOrderingFilePath).writeText(jsonDict.toString())
    }

    private fun loadUserToItemOrdering() {
        val jsonDict = File(userToItemOrderingFilePath).readText()
        userToItemOrdering = Json.decodeFromString(jsonDict)
    }
}
