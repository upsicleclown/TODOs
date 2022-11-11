package controllers

import TODOApplication
import client.TODOClient
import commands.CreateItemCommand
import commands.CreateItemLabelCommand
import commands.DeleteItemCommand
import commands.DeleteItemLabelCommand
import commands.EditItemCommand
import commands.EditItemLabelCommand
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
    private var focusedItem: Item? = null

    init {
        app = todoApp
    }

    fun reloadGroupView() {
        items = todoClient.getItems()
        labels = todoClient.getLabels()
        view?.refreshWithItems(currentGroup, items)
    }

    private fun refreshLabels() {
        labels = todoClient.getLabels()
    }

    fun loadGroup(group: Group?) {
        if (group === null) { return }
        currentGroup = group
        reloadGroupView()
    }

    fun createItem(item: Item) {
        val createItemCommand = CreateItemCommand(item, this)
        app?.commandHandler?.execute(createItemCommand)
        reloadGroupView()
    }

    fun editItem(newItem: Item, originalItem: Item) {
        val editItemCommand = EditItemCommand(newItem, originalItem, this)
        app?.commandHandler?.execute(editItemCommand)
        reloadGroupView()
    }

    fun deleteItem(item: Item) {
        val deleteItemCommand = DeleteItemCommand(item, this)
        app?.commandHandler?.execute(deleteItemCommand)
        reloadGroupView()
    }

    fun createItemLabel(existingLabel: Boolean, newLabel: Label, item: Item) {
        val createItemLabelCommand = CreateItemLabelCommand(existingLabel, newLabel, item, this)
        app?.commandHandler?.execute(createItemLabelCommand)
        reloadGroupView()
    }

    fun editItemLabel(existingLabel: Boolean, newLabel: Label, originalLabel: Label, item: Item) {
        val editItemLabelCommand = EditItemLabelCommand(existingLabel, newLabel, originalLabel, item, this)
        app?.commandHandler?.execute(editItemLabelCommand)
        reloadGroupView()
    }

    fun deleteItemLabel(label: Label, item: Item) {
        val deleteItemLabelCommand = DeleteItemLabelCommand(label, item, this)
        app?.commandHandler?.execute(deleteItemLabelCommand)
        reloadGroupView()
    }

    fun labels(): List<Label> { return labels }

    // view management
    fun addView(groupView: GroupView) {
        view = groupView
    }

    fun focusItem(item: Item) {
        focusedItem = item
    }

    fun clearFocus() {
        focusedItem = null
    }

    /**
     * Returns the focused item in the list view if any.
     */
    fun getFocusedItem(): Item? {
        return focusedItem
    }
}
