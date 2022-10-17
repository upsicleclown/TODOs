package server

import models.Group
import models.Item
import models.Label
import models.WindowSettings
import org.springframework.stereotype.Service
import server.database.FileDB

@Service
class Service {
    // Window settings will remain saved in file. File path could potentially become a secret.
    private val windowSettingDBFilePath = this.javaClass.classLoader.getResource("server/database/window_settings.json")!!.path

    // TODO: remove these once FileDB replaced by real DB
    private val itemDBFilepath = this.javaClass.classLoader.getResource("server/database/items.json")!!.path
    private val labelDBFilepath = this.javaClass.classLoader.getResource("server/database/labels.json")!!.path
    private val groupDBFilepath = this.javaClass.classLoader.getResource("server/database/groups.json")!!.path

    private var fileDB: FileDB = FileDB(windowSettingDBFilePath, itemDBFilepath, labelDBFilepath, groupDBFilepath)

    init {
        fileDB.loadWindowSettings()
        fileDB.loadItems()
        fileDB.loadLabels()
        fileDB.loadGroups()
    }

    /*
       This method must be called when the client is done using the service.
     */
    fun close() {
        fileDB.saveWindowSettings()
        fileDB.saveItems()
        fileDB.saveLabels()
        fileDB.saveGroups()
    }

    /*
       Window setting endpoints
     */
    fun getWindowSettings(): WindowSettings {
        return fileDB.getWindowSettings()
    }

    /*
        WindowSettings will now become new settings. Hence, ensure new window settings has all fields it needs to have.
     */
    fun editWindowSettings(newWindowSettings: WindowSettings) {
        fileDB.editWindowSettings(newWindowSettings)
    }

    /*
       Item endpoints
     */
    fun addItem(item: Item) {
        // if item has label ids, ensure they exist.
        if (item.labelIds.isNotEmpty()) {
            val labelIdsNotInDb: List<Int> = getLabelIdsNotInDb(item.labelIds)
            if (labelIdsNotInDb.isNotEmpty()) {
                throw IllegalArgumentException(
                    "Cannot add item with label ids $labelIdsNotInDb " +
                        "since these label ids do not exist in the database."
                )
            }
        }
        fileDB.addItem(item)
    }

    /*
        Item with provided id will now become new item. Hence, ensure new item has all fields it needs to have.
     */
    fun editItem(itemId: Int, newItem: Item) {
        // if item has label ids, ensure they exist.
        if (newItem.labelIds.isNotEmpty()) {
            val labelIdsNotInDb: List<Int> = getLabelIdsNotInDb(newItem.labelIds)
            if (labelIdsNotInDb.isNotEmpty()) {
                throw IllegalArgumentException(
                    "Cannot add item with label ids $labelIdsNotInDb " +
                        "since these label ids do not exist in the database."
                )
            }
        }
        fileDB.editItem(itemId, newItem)
    }

    fun removeItem(itemId: Int) {
        fileDB.removeItem(itemId)
    }

    fun getItems(): List<Item> {
        return fileDB.getItems()
    }

    /*
       Private helpers for labels.
     */
    private fun getLabelIdsNotInDb(labelIds: List<Int>): List<Int> {
        val labelIdsNotInDB = mutableListOf<Int>()
        val labelIdsInDb: List<Int> = fileDB.getLabels().map { label: Label -> label.id }
        for (labelId in labelIds) {
            if (!labelIdsInDb.contains(labelId)) {
                labelIdsNotInDB.add(labelId)
            }
        }
        return labelIdsNotInDB
    }

    /*
       Label endpoints
     */
    fun addLabel(label: Label) {
        fileDB.addLabel(label)
    }

    /*
        Label with provided id will now become new label. Hence, ensure new label has all fields it needs to have.
     */
    fun editLabel(labelId: Int, newLabel: Label) {
        fileDB.editLabel(labelId, newLabel)
    }

    fun removeLabel(labelId: Int) {
        // Remove label id from items with this label
        val itemsWithLabel: List<Item> = fileDB.getItems().filter { item: Item -> item.labelIds.contains(labelId) }
        for (item in itemsWithLabel) {
            item.labelIds.remove(labelId)
            fileDB.editItem(item.id, item)
        }
        // Remove groups with this label.
        val groupsWithLabel: List<Group> = fileDB.getGroups().filter { group: Group -> group.labelIds.contains(labelId) }
        for (group in groupsWithLabel) {
            group.labelIds.remove(labelId)
            fileDB.editGroup(group.id, group)
        }
        // Remove label.
        fileDB.removeLabel(labelId)
    }

    fun getLabels(): List<Label> {
        return fileDB.getLabels()
    }

    /*
       Group endpoints
     */
    fun addGroup(group: Group) {
        // if group has label ids, ensure they exist.
        if (group.labelIds.isNotEmpty()) {
            val labelIdsNotInDb: List<Int> = getLabelIdsNotInDb(group.labelIds)
            if (labelIdsNotInDb.isNotEmpty()) {
                throw IllegalArgumentException(
                    "Cannot add group with label ids $labelIdsNotInDb " +
                        "since these label ids do not exist in the database."
                )
            }
        }
        fileDB.addGroup(group)
    }

    /*
        Group with provided id will now become new group. Hence, ensure new group has all fields it needs to have.
     */
    fun editGroup(groupId: Int, newGroup: Group) {
        // if group has label ids, ensure they exist.
        if (newGroup.labelIds.isNotEmpty()) {
            val labelIdsNotInDb: List<Int> = getLabelIdsNotInDb(newGroup.labelIds)
            if (labelIdsNotInDb.isNotEmpty()) {
                throw IllegalArgumentException(
                    "Cannot edit group with label ids $labelIdsNotInDb " +
                        "since these label ids do not exist in the database."
                )
            }
        }
        fileDB.editGroup(groupId, newGroup)
    }

    fun removeGroup(groupId: Int) {
        fileDB.removeGroup(groupId)
    }

    fun getGroups(): List<Group> {
        return fileDB.getGroups()
    }
}
