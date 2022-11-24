package controllers

import TODOApplication
import bindings.GroupProperty
import bindings.ItemListProperty
import bindings.ItemProperty
import bindings.LabelListProperty
import cache.Cache
import client.TODOClient
import commands.CreateItemCommand
import commands.CreateItemLabelCommand
import commands.CreateLabelCommand
import commands.DeleteItemCommand
import commands.DeleteItemLabelCommand
import commands.DeleteLabelCommand
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
class GroupViewController(todoApp: TODOApplication, private val cache: Cache) {
    private var app: TODOApplication? = null
    private var view: GroupView? = null
    private val todoClient = TODOClient
    var displayItemList: ObservableList<Item>

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
            resetSortOrderIfNeeded()
        }
    }

    private fun reloadDisplayItemList(newItemList: ObservableList<Item>) {
        val comparator = compareBy<Item> {
            when (view?.sortOrder!!.attribute) {
                GroupView.Attribute.IS_COMPLETED -> it.isCompleted
                GroupView.Attribute.EDT_DUEDATE -> it.edtDueDate
                GroupView.Attribute.PRIORITY -> it.priority
                GroupView.Attribute.CUSTOM -> null // comparator not used for custom.
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

        // if custom sorting, order by cached ordering, else use `sortOrder`.
        var sortedList: List<Item>
        if (view?.sortOrder!!.attribute == GroupView.Attribute.CUSTOM) {
            sortedList = orderByCustom(filteredList.toMutableList())
        } else {
            sortedList = filteredList.sortedWith(comparator)
            if (view?.sortOrder!!.isDesc) {
                sortedList = sortedList.reversed()
            }
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

    fun createLabel(collision: Boolean, newLabel: Label) {
        val createLabelCommand = CreateLabelCommand(collision, newLabel)
        app?.commandHandler?.execute(createLabelCommand)
    }

    fun editLabel(collision: Boolean, newLabel: Label, originalLabel: Label) {
        val editLabelCommand = EditLabelCommand(collision, newLabel, originalLabel)
        app?.commandHandler?.execute(editLabelCommand)
    }

    fun deleteLabel(label: Label) {
        val deleteLabelCommand = DeleteLabelCommand(label)
        app?.commandHandler?.execute(deleteLabelCommand)
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

    /**
     * Orders the provided item list based on the cached item order for the current group.
     * The list is left un-touched if nothing is cached for the current group or the current group is null.
     */
    private fun orderByCustom(itemList: MutableList<Item>): List<Item> {
        val currentGroupId: Int = currentGroupProperty.value?.id ?: return itemList
        val itemIdOrdering: List<Int> = cache.getGroupToItemOrdering()[currentGroupId] ?: return itemList

        // for all items that have a saved order, order them.
        val orderedItemList = mutableListOf<Item>()
        for (itemId in itemIdOrdering) {
            val itemIdList: List<Int> = itemList.map { item: Item? -> item!!.id }
            if (itemIdList.contains(itemId)) {
                val nextItem = itemList[itemIdList.indexOf(itemId)]
                orderedItemList.add(nextItem)
                itemList.remove(nextItem)
            }
        }

        // for all items that do not have a saved order, if any, append them after ordering is done.
        for (item in itemList) {
            orderedItemList.add(item)
        }

        return orderedItemList
    }

    /**
     * If current group is default group and sort order is custom, reset to default sort order.
     * (We do not support custom ordering for the default group.)
     */
    private fun resetSortOrderIfNeeded() {
        if (currentGroupProperty.value == null && view?.sortOrder!!.attribute == GroupView.Attribute.CUSTOM) {
            setSortOrder(GroupView.SortOrder())
        }
    }
}
