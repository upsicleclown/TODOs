package controllers

import TODOApplication
import client.TODOClient
import commands.CreateItemCommand
import commands.DeleteItemCommand
import commands.EditItemCommand
import models.Group
import models.Item
import views.GroupView

/**
 * todoApp is passed as a parameter so that the GroupViewController can access the app's commandHandler
 */
class GroupViewController(todoApp: TODOApplication) {
    private var app: TODOApplication? = null
    private var items = listOf<Item>()
    private var view: GroupView? = null
    private val todoClient = TODOClient()
    private var currentGroup: Group? = null

    init {
        app = todoApp
        items = todoClient.getItems()
    }

    private fun reloadGroupView() {
        items = todoClient.getItems()
        view?.refreshWithItems(currentGroup!!, items)
    }

    fun loadGroup(group: Group?) {
        if (group === null) { return }
        currentGroup = group
        reloadGroupView()
    }

    fun createItem(itemTitle: String) {
        val createItemCommand = CreateItemCommand(itemTitle)
        app?.commandHandler?.execute(createItemCommand)
        reloadGroupView()
    }

    fun editItem(newItem: Item, originalItem: Item) {
        val editItemCommand = EditItemCommand(newItem, originalItem)
        app?.commandHandler?.execute(editItemCommand)
        reloadGroupView()
    }

    fun deleteItem(item: Item) {
        val deleteItemCommand = DeleteItemCommand(item)
        app?.commandHandler?.execute(deleteItemCommand)
        reloadGroupView()
    }

    // view management
    fun addView(groupView: GroupView) {
        view = groupView
    }
}
