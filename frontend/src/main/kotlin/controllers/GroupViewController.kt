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
import commands.EditLabelCommand
import commands.EditSortOrderCommand
import javafx.collections.FXCollections
import javafx.collections.ObservableList
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
    private var displayItemList: ObservableList<Item>

    var displayItemListProperty: ItemListProperty
    var itemListProperty: ItemListProperty
    var labelListProperty: LabelListProperty
    var currentGroupProperty = GroupProperty()
    var focusedItemProperty = ItemProperty()

    init {
        app = todoApp
        itemListProperty = todoClient.itemListProperty
        labelListProperty = todoClient.labelListProperty

        displayItemList = FXCollections.observableArrayList(itemListProperty.value)
        displayItemListProperty = ItemListProperty(displayItemList)

        itemListProperty.addListener { _, _, newItemList ->
            reloadDisplayItemList(newItemList)
        }
        currentGroupProperty.addListener { _, _, _ ->
            reloadDisplayItemList(itemListProperty.value)
        }
    }

    private fun reloadDisplayItemList(newItemList: ObservableList<Item>) {
        val comparator = compareBy<Item> {
            when (view?.sortOrder!!.attribute) {
                GroupView.Attribute.IS_COMPLETED -> it.isCompleted
                GroupView.Attribute.EDT_DUEDATE -> it.edtDueDate
                GroupView.Attribute.PRIORITY -> it.priority
            }
        }

        val filteredList = if (currentGroupProperty.value != null) {
            newItemList.filter {
                (if (currentGroupProperty.value!!.filter.isCompleted != null) currentGroupProperty.value!!.filter.isCompleted == it.isCompleted else true) &&

                    (
                        if (currentGroupProperty.value!!.filter.edtStartDateRange != null) {
                            if (it.edtDueDate != null) {
                                currentGroupProperty.value!!.filter.edtStartDateRange!! <= it.edtDueDate!!
                            } else {
                                false
                            }
                        } else {
                            true
                        }
                        ) &&

                    (
                        if (currentGroupProperty.value!!.filter.edtEndDateRange != null) {
                            if (it.edtDueDate != null) {
                                currentGroupProperty.value!!.filter.edtEndDateRange!! >= it.edtDueDate!!
                            } else {
                                false
                            }
                        } else {
                            true
                        }
                        ) &&

                    (if (currentGroupProperty.value!!.filter.priorities.size != 0) it.priority in currentGroupProperty.value!!.filter.priorities else true) &&
                    currentGroupProperty.value!!.filter.labelIds.all { labelId -> labelId in it.labelIds }
            }
        } else {
            newItemList.toList()
        }

        var sortedList: List<Item> = filteredList.sortedWith(comparator)

        if (view?.sortOrder!!.isDesc) {
            sortedList = sortedList.reversed()
        }

        displayItemList.setAll(
            sortedList
        )
    }

    fun setSortOrder(newSortOrder: GroupView.SortOrder) {
        view?.sortOrder = newSortOrder
        reloadDisplayItemList(displayItemList)
    }

    fun loadGroup(group: Group?) {
        currentGroupProperty.set(group)
    }

    fun createItem(item: Item) {
        val createItemCommand = CreateItemCommand(item, currentGroupProperty.value)
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

    fun editLabel(collision: Boolean, newLabel: Label, originalLabel: Label) {
        val editLabelCommand = EditLabelCommand(collision, newLabel, originalLabel)
        app?.commandHandler?.execute(editLabelCommand)
    }
    fun editSortOrder(newSortOrder: GroupView.SortOrder, oldSortOrder: GroupView.SortOrder) {
        val editSortOrderCommand = EditSortOrderCommand(newSortOrder, oldSortOrder, this)
        app?.commandHandler?.execute(editSortOrderCommand)
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
