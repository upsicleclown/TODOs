package server

import models.Group
import models.Item
import server.database.FileDB
import java.lang.IllegalArgumentException

class Service (itemDBFilepath: String, groupDBFilePath: String) {
    var fileDB: FileDB = FileDB(itemDBFilepath, groupDBFilePath)

    init {
        fileDB.loadItems()
        fileDB.loadGroups()
    }

    /*
       This method must be called when the client is done using the service.
     */
    fun close() {
        fileDB.saveItems()
        fileDB.saveGroups()
    }

    /*
       Item endpoints
     */
    fun addItem(item: Item) {
        // if item has a group id, ensure it exists.
        if (item.groupId != null && !doesGroupExist(item.groupId!!)) {
            throw IllegalArgumentException(
                "Cannot add item with group id ${item.groupId} since this group id does not exist in the database."
            )
        }
        fileDB.addItem(item)
    }

    /*
        Item with provided id will now become new item. Hence, ensure new item has all fields it needs to have.
     */
    fun editItem(itemId: Int, newItem: Item) {
        // if item has a group id, ensure it exists.
        if (newItem.groupId != null && !doesGroupExist(newItem.groupId!!)) {
            throw IllegalArgumentException(
                "Cannot add item with group id ${newItem.groupId} since this group id does not exist in the database."
            )
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
       Private helpers for groups.
     */
    private fun doesGroupExist(groupId: Int): Boolean {
        return fileDB.getGroups().map { group: Group -> group.id }.contains(groupId)
    }

    /*
       Group endpoints
     */
    fun addGroup(group: Group) {
        fileDB.addGroup(group)
    }

    /*
        Group with provided id will now become new group. Hence, ensure new group has all fields it needs to have.
     */
    fun editGroup(groupId: Int, newGroup: Group) {
        fileDB.editGroup(groupId, newGroup)
    }

    fun removeGroup(groupId: Int) {
        // Remove group id from items in this group.
        val itemsInGroup = fileDB.getItems().filter { item: Item -> item.groupId == groupId }
        for (item in itemsInGroup) {
            item.groupId = null
            fileDB.editItem(item.id, item)

        }
        // remove group.
        fileDB.removeGroup(groupId)
    }

    fun getGroups(): List<Group> {
        return fileDB.getGroups()
    }

}