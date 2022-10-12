package server.database

import models.Group
import models.Item
import models.Label

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.encodeToJsonElement
import java.io.File

/*
   In file database represented as a JSON.
 */
class FileDB(private val itemFilepath: String, private val labelFilepath: String, private val groupFilepath: String) {
    /* Properties related to items. */
    private var items: MutableList<Item> = mutableListOf()
    private var nextItemId: Int = 1

    /* Properties related to labels. */
    private var labels: MutableList<Label> = mutableListOf()
    private var nextLabelId: Int = 1

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
                item.labelIds = newItem.labelIds
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

    /* Methods related to labels. */
    fun addLabel(label: Label) {
        label.id = nextLabelId
        nextLabelId += 1
        labels.add(label)
    }

    fun removeLabel(labelId: Int) {
        for (currLabel in labels) {
            if (currLabel.id == labelId) {
                labels.remove(currLabel)
                return
            }
        }
        throw IllegalArgumentException("Could not remove label with id $labelId since no such label in database.")
    }

    /*
        Label with provided id will now become new label. Hence, ensure new label has all fields it needs to have.
     */
    fun editLabel(labelId: Int, newLabel: Label) {
        for (label in labels) {
            if (label.id == labelId) {
                label.name = newLabel.name
                return
            }
        }
        throw IllegalArgumentException("Could not edit label with id $labelId since no such label in database.")
    }

    fun getLabels(): List<Label> {
        return labels
    }

    fun saveLabels() {
        val jsonList: List<JsonElement> = groups.map { label -> Json.encodeToJsonElement(label) }
        File(labelFilepath).writeText(jsonList.toString())
    }

    fun loadLabels() {
        val jsonList = File(labelFilepath).readText()
        labels.addAll(Json.decodeFromString(jsonList))
        nextLabelId = labels.maxOf { label -> label.id } + 1
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
        val jsonList: List<JsonElement> = groups.map { group -> Json.encodeToJsonElement(group) }
        File(groupFilepath).writeText(jsonList.toString())
    }

    fun loadGroups() {
        val jsonList = File(groupFilepath).readText()
        groups.addAll(Json.decodeFromString(jsonList))
        nextGroupId = groups.maxOf { group -> group.id } + 1
    }

}