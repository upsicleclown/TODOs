package controllers

import TODOApplication
import bindings.GroupProperty
import bindings.ItemListProperty
import bindings.ItemProperty
import bindings.LabelListProperty
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
    private var view: GroupView? = null
    private val todoClient = TODOClient
    var itemListProperty: ItemListProperty
    var labelListProperty: LabelListProperty
    var currentGroupProperty = GroupProperty()
    var focusedItemProperty = ItemProperty()

    init {
        app = todoApp
        itemListProperty = todoClient.itemListProperty
        labelListProperty = todoClient.labelListProperty
    }
    fun loadGroup(group: Group?) {
        if (group === null) {
            return
        }
        currentGroupProperty.set(group)
    }

    fun createItem(item: Item) {
        val createItemCommand = CreateItemCommand(item)
        app?.commandHandler?.execute(createItemCommand)
    }

    fun editItem(newItem: Item, originalItem: Item) {
        val editItemCommand = EditItemCommand(newItem, originalItem)
        app?.commandHandler?.execute(editItemCommand)
    }

    fun deleteItem(item: Item) {
        val deleteItemCommand = DeleteItemCommand(item)
        app?.commandHandler?.execute(deleteItemCommand)
    }

    fun createItemLabel(existingLabel: Boolean, newLabel: Label, item: Item) {
        val createItemLabelCommand = CreateItemLabelCommand(existingLabel, newLabel, item)
        app?.commandHandler?.execute(createItemLabelCommand)
    }

    fun editItemLabel(existingLabel: Boolean, newLabel: Label, originalLabel: Label, item: Item) {
        val editItemLabelCommand = EditItemLabelCommand(existingLabel, newLabel, originalLabel, item)
        app?.commandHandler?.execute(editItemLabelCommand)
    }

    fun deleteItemLabel(label: Label, item: Item) {
        val deleteItemLabelCommand = DeleteItemLabelCommand(label, item)
        app?.commandHandler?.execute(deleteItemLabelCommand)
    }

    fun labels(): LabelListProperty {
        return labelListProperty
    }

    // view management
    fun addView(groupView: GroupView) {
        view = groupView
    }

    fun focusItem(item: Item) {
        focusedItemProperty.set(item)
    }

    fun clearFocus() {
        focusedItemProperty.set(null)
    }
}
