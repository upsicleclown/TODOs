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
import commands.LogOutUserCommand
import javafx.beans.property.SimpleListProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.scene.Node
import models.BooleanOperator
import models.Filter
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
    private val groupFilterLabelList: ObservableList<Int> = FXCollections.observableArrayList()
    var displayItemList: ObservableList<Item>

    var displayItemListProperty: ItemListProperty
    var itemListProperty: ItemListProperty
    var labelListProperty: LabelListProperty
    var currentGroupProperty = GroupProperty()
    var groupFilterLabelListProperty = SimpleListProperty(groupFilterLabelList)
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
        currentGroupProperty.addListener { _, oldGroup, _ ->
            saveSortOrderIfNeeded(oldGroup, displayItemList)
            reloadDisplayItemList(itemListProperty.value)
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

                    (if (it.priority == null) currentGroupProperty.value!!.filter.priorities.size == 0 else it.priority in currentGroupProperty.value!!.filter.priorities) &&
                    (
                        when (currentGroupProperty.value!!.filter.labelBooleanOperator) {
                            BooleanOperator.AND -> currentGroupProperty.value!!.filter.labelIds.all { labelId -> labelId in it.labelIds }
                            BooleanOperator.OR -> currentGroupProperty.value!!.filter.labelIds.any { labelId -> labelId in it.labelIds }
                            BooleanOperator.NOT -> !currentGroupProperty.value!!.filter.labelIds.any { labelId -> labelId in it.labelIds }
                            else -> throw Exception("Boolean operator not supported in group filter!")
                        }
                        )
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

    fun reloadCurrentGroup(group: Group) {
        currentGroupProperty.value = group
        reloadDisplayItemList(itemListProperty.value)
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

    fun editCurrentGroupFilter(newFilter: Filter) {
        // This is not implemented as a command, it is a workaround.
        // In order to support undo, the currentGroupProperty would have to be listenable in a meaningful way for the UI to be up-to-date.
        // Since listeners perform equality checks and equality for a Group is by id, changing a Group's filter does not trigger an event
        val todoClient = TODOClient
        val newGroup = currentGroupProperty.value.copy()
        newGroup.filter = newFilter
        todoClient.editGroup(currentGroupProperty.value.id, newGroup)
        reloadCurrentGroup(newGroup)
        groupFilterLabelListProperty.setAll(newGroup.filter.labelIds)
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
     * If the current group is null, which assumes the provided item list is for the default view (all items),
     * this method orders the list based on the cached item order for the logged-in user.
     *
     * If the current group is not null, which assumes the provided item list is for that group,
     * this method orders the list based on the cached item order for the group.
     *
     * The list is left un-touched if nothing is cached.
     */
    private fun orderByCustom(itemList: MutableList<Item>): List<Item> {
        // Look at the right cache.
        val itemIdOrdering: List<Int>? = if (currentGroupProperty.value == null) {
            cache.getUserToItemOrdering()[todoClient.getLoggedInUser().id]
        } else {
            cache.getGroupToItemOrdering()[currentGroupProperty.value.id]
        }

        // Nothing is cached, return.
        if (itemIdOrdering == null) return itemList

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
     * If sort order is not custom, nothing happens here.
     *
     * If the current group is null, which assumes the provided item list is for the default view (all items),
     * this method saves the provided item order for the logged-in user.
     *
     * If the current group is not null, which assumes the provided item list is for that group,
     * this method saves the provided item order for the provided group.
     */
    private fun saveSortOrderIfNeeded(group: Group?, items: List<Item>) {
        if (view?.sortOrder!!.attribute != GroupView.Attribute.CUSTOM) return
        if (group == null) {
            val userToItemOrdering: HashMap<Int, List<Int>> = cache.getUserToItemOrdering()
            userToItemOrdering[todoClient.getLoggedInUser().id] = items.map { item -> item.id }
            cache.editUserToItemOrdering(userToItemOrdering)
        } else {
            val groupToItemOrdering: HashMap<Int, List<Int>> = cache.getGroupToItemOrdering()
            groupToItemOrdering[group.id] = items.map { item -> item.id }
            cache.editGroupToItemOrdering(groupToItemOrdering)
        }
    }

    /**
     * `saveSortOrderIfNeeded` wrapper for the app to save the current sort order if need be on app stop.
     */
    fun saveCurrentSortOrderIfNeeded() {
        if (currentGroupProperty.value != null) {
            saveSortOrderIfNeeded(currentGroupProperty.value, displayItemList)
        }
    }

    /**
     * Given the item's new Y position, it updates the `displayItemList` to place said item at the right position.
     */
    fun setItemNewYPosition(item: Item, newYPosition: Double) {
        val itemContainers: ObservableList<Node> = view?.getItemContainers()!!
        var newIdx = 0 // new index the item will be placed at in the list.

        // assume item is placed at the start so skip first item.
        for (itemContainer in itemContainers.subList(1, itemContainers.size)) {
            // if y coordinate is greater or equal than current item, it will be placed after in the list.
            // new index cannot ge greater than the last index.
            if (newYPosition >= itemContainer.layoutY && newIdx < itemContainers.size - 1) {
                newIdx += 1
                continue
            }
            // if y coordinate is less than current item, break.
            break
        }
        setItemNewIndex(item, newIdx)
    }

    /**
     * Given the item's new index, it updates the `displayItemList` to place said item at the index if within the range.
     */
    private fun setItemNewIndex(item: Item, newIndex: Int) {
        // switch only if required and within the range.
        if (newIndex != displayItemList.indexOf(item) && newIndex >= 0 && newIndex < displayItemList.size) {
            displayItemList.remove(item)
            displayItemList.add(newIndex, item)
            view!!.setCustomOrder()
        }
    }

    /**
     * Increments the provided item's index if `downwards` true else decrements it.
     */
    fun moveItem(item: Item, downwards: Boolean) {
        val originalIndex = displayItemList.indexOf(item)
        // downwards mean further into the list, so increment index.
        val indexChange = if (downwards) 1 else -1
        setItemNewIndex(item, originalIndex + indexChange)
    }

    /**
     * Cursor functions
     */
    fun setOpenHandCursor() {
        app?.setOpenHandCursor()
    }

    fun setClosedHandCursor() {
        app?.setClosedHandCursor()
    }

    fun resetCursor() {
        app?.resetCursor()
    }

    /**
     * Logout User
     */
    fun logoutUser() {
        saveCurrentSortOrderIfNeeded()
        app?.commandHandler?.execute(LogOutUserCommand())
        app?.setLoginView()
    }
}
