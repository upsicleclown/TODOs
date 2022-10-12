package controllers

import models.Item;
import models.Group;
import models.InvalidateGroupViewEvent
import models.InvalidateGroupViewRequest
import tornadofx.*;

class GroupViewController: Controller() {
    private var items = listOf<Item>()

    init {
        subscribe<InvalidateGroupViewRequest> {event ->
            /* TODO: will eventually want to make this async and fire data once complete */
            loadGroup(event.group)
            fire(InvalidateGroupViewEvent(items))
        }
    }

    fun loadGroup(group: Group): List<Item> {
        /* Should be used for fetching group info from server necessary to render */
        items = listOf(Item("Task 1", isCompleted = true), Item("Task 2", isCompleted = false))
        return items
    }

    fun addItem() {}

    fun deleteItem(item: Item) {}

    fun addLabelToItem(item: Item) {}

    fun changeItemColor(item: Item) {}
}