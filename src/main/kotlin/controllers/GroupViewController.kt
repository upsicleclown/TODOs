package controllers

import java.util.*
import models.Item;
import models.Group;
import models.InvalidateGroupViewEvent
import models.InvalidateGroupViewRequest
import tornadofx.*;

class GroupViewController: Controller() {
    private var items = listOf<Item>()

    init {
        println("gvc init reached")

        subscribe<InvalidateGroupViewRequest> {event ->
            println("received invalidate group view request")
            loadGroup(event.group)
            fire(InvalidateGroupViewEvent(items))
            println("fired group view event")
        }
    }

    fun loadGroup(group: Group) {
        // Should be used for fetching group info from server necessary to render
        items = listOf(Item("Task 1", isCompleted = true), Item("Task 2", isCompleted = false))
    }

    fun addItem() {}

    fun deleteItem(item: Item) {}

    fun addLabelToItem(item: Item) {}

    fun changeItemColor(item: Item) {}
}