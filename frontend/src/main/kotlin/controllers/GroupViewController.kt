package controllers

import TODOApplication
import client.TODOClient
import commands.CreateItemCommand
import commands.DeleteItemCommand
import commands.EditItemCommand
import models.Group
import models.Item
import models.Label
import views.GroupView

/**
 * todoApp is passed as a parameter so that the GroupViewController can access the app's commandHandler
 */
class GroupViewController(todoApp: TODOApplication) {
    private var app: TODOApplication? = null
    private var items = listOf<Item>()
    private var labels = listOf<Label>()
    private var view: GroupView? = null
    private val todoClient = TODOClient()
    private var currentGroup: Group? = null

    init {
        app = todoApp
        items = todoClient.getItems()
        labels = todoClient.getLabels()
    }

    private fun reloadGroupView() {
        items = todoClient.getItems()
        labels = todoClient.getLabels()
        view?.refreshWithItems(currentGroup!!, items)
    }

    fun refreshLabels() {
        labels = todoClient.getLabels()
    }

    fun loadGroup(group: Group?) {
        if (group === null) { return }
        currentGroup = group
        reloadGroupView()
    }

    fun createItem(item: Item) {
        val createItemCommand = CreateItemCommand(item)
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

    fun labels(): List<Label> { return labels }

    fun createLabel(label: Label) {
        todoClient.createLabel(label)
        refreshLabels()
    }

    // view management
    fun addView(groupView: GroupView) {
        view = groupView
    }
}
