package server.database

import models.Group
import models.Item

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.encodeToJsonElement
import java.io.File

/*
   In file database represented as a JSON.
 */
class FileDB(private val itemFilepath: String, val groupFilepath: String) {
    /* Properties related to items. */
    private var items: MutableList<Item> = mutableListOf()
    private var nextItemId: Int = 1

    /* Properties related to groups. */
    private var groups: MutableList<Group> = mutableListOf()
    private var nextGroupId: Int = 1

    /* Methods related to items. */
    fun addItem(item: Item) {
        item.id = nextItemId
        nextItemId += 1
        items.add(item)
    }

    fun removeItem(itemId: Int) {
        for (currItem in items) {
            if (currItem.id == itemId) {
                items.remove(currItem)
                return
            }
        }
        throw IllegalArgumentException("Could not remove item with id $itemId since no such item in database.")
    }

    /*
        Item with provided id will now become new item. Hence, ensure new item has all fields it needs to have.
     */
    fun editItem(itemId: Int, newItem: Item) {
        for (item in items) {
            if (item.id == itemId) {
                item.title = newItem.title
                item.isCompleted = newItem.isCompleted
                item.groupId = newItem.groupId
                return
            }
        }
        throw IllegalArgumentException("Could not edit item with id $itemId since no such item in database.")
    }

    fun getItems(): List<Item> {
        return items
    }

    fun saveItems() {
        val jsonList: List<JsonElement> = items.map { item -> Json.encodeToJsonElement(item) }
        File(itemFilepath).writeText(jsonList.toString())
    }

    fun loadItems() {
        val jsonList = File(itemFilepath).readText()
        items.addAll(Json.decodeFromString(jsonList))
        nextItemId = items.maxOf { item -> item.id } + 1
    }

    /* Methods related to groups. */
    fun addGroup(group: Group) {
        group.id = nextGroupId
        nextGroupId += 1
        groups.add(group)
        println(groups)
    }

    fun removeGroup(groupId: Int) {
        for (currGroup in groups) {
            if (currGroup.id == groupId) {
                groups.remove(currGroup)
                return
            }
        }
        throw IllegalArgumentException("Could not remove group with id $groupId since no such group in database.")
    }

    /*
        Group with provided id will now become new group. Hence, ensure new group has all fields it needs to have.
     */
    fun editGroup(groupId: Int, newGroup: Group) {
        for (group in groups) {
            if (group.id == groupId) {
                group.name = newGroup.name
                return
            }
        }
        throw IllegalArgumentException("Could not edit group with id $groupId since no such group in database.")
    }

    fun getGroups(): List<Group> {
        return groups
    }

    fun saveGroups() {
        val jsonList: List<JsonElement> = groups.map { item -> Json.encodeToJsonElement(item) }
        File(groupFilepath).writeText(jsonList.toString())
    }

    fun loadGroups() {
        val jsonList = File(groupFilepath).readText()
        groups.addAll(Json.decodeFromString(jsonList))
        nextGroupId = groups.maxOf { group -> group.id } + 1
    }

}