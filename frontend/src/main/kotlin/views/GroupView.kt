package views

import controllers.GroupViewController
import javafx.beans.value.ChangeListener
import javafx.event.EventHandler
import javafx.scene.control.Button
import javafx.scene.control.ComboBox
import javafx.scene.control.Label
import javafx.scene.control.ScrollPane
import javafx.scene.control.TextField
import javafx.scene.input.MouseEvent
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import models.Item
import kotlin.properties.Delegates

class GroupView(private val controller: GroupViewController) : VBox(36.0) {

    enum class Attribute(val value: String) {
        IS_COMPLETED("Completed"), EDT_DUEDATE("Due Date"), PRIORITY("Priority");

        companion object {
            private val map = values().associateBy { it.value }
            infix fun from(value: String) = map[value]
        }
    }

    data class SortOrder(var attribute: Attribute = Attribute.IS_COMPLETED, var isDesc: Boolean = true)

    var sortOrder: SortOrder by Delegates.observable(SortOrder()) { _, _, _ -> loadSortOrderPicker() }
    val sortOrderAttributePicker = ComboBox<String>()
    val sortOrderIsDescButton = Button()
    val sortOrderPicker = HBox()
    private val itemListScrollContainer = ScrollPane()
    private val itemListContainer = VBox(36.0)
    private val itemCreationField = TextField()
    private var currentGroupName = Label("")

    init {

        setUpSortOrderPicker()
        /* region styling */
        styleClass.add("group")
        currentGroupName.styleClass.addAll("group__title", "title-max")
        itemCreationField.styleClass.addAll("h2", "group__create-item")
        itemListScrollContainer.styleClass.addAll("group__item-list__container")
        itemListContainer.styleClass.addAll("group__item-list")
        /* end region styling */

        /* region event filters */
        // when enter is pressed
        itemCreationField.setOnAction {
            controller.createItem(Item(itemCreationField.text, false))
            itemCreationField.text = ""
        }

        // when clicking outside of an item, unfocus that item
        itemListContainer.addEventHandler(
            MouseEvent.MOUSE_CLICKED,
            EventHandler<MouseEvent> {
                requestFocus()
                controller.clearFocus()
            }
        )

        this.addEventHandler(
            MouseEvent.MOUSE_CLICKED,
            EventHandler<MouseEvent> {
                requestFocus()
                controller.clearFocus()
            }
        )
        /* end region event filters */

        /* region view setup */
        itemCreationField.promptText = "Create a new item..."

        itemListScrollContainer.isFitToWidth = true
        itemListScrollContainer.content = itemListContainer
        itemListScrollContainer.hmax = 0.0
        itemListScrollContainer.hbarPolicy = ScrollPane.ScrollBarPolicy.NEVER

        children.addAll(currentGroupName, sortOrderPicker, itemCreationField, itemListScrollContainer)
        /* end region view setup */

        /* region data bindings */
        controller.currentGroupProperty.addListener { _, _, newGroup ->
            currentGroupName.text = newGroup.name
        }

        controller.displayItemListProperty.addListener { _, _, newItemList ->
            itemListContainer.children.setAll(
                newItemList.map {
                        item ->
                    ItemView(controller, item)
                }
            )
        }
        /* end region data bindings */
    }

    private fun loadSortOrderPicker() {
        sortOrderIsDescButton.text = if (sortOrder.isDesc) "↑" else "↓"
        sortOrderAttributePicker.value = sortOrder.attribute.value
    }

    private fun setUpSortOrderPicker() {
        loadSortOrderPicker()

        sortOrderIsDescButton.setOnAction {
            val newIsDesc = !sortOrder.isDesc
            controller.editSortOrder(SortOrder(sortOrder.attribute, newIsDesc), sortOrder)
        }

        sortOrderAttributePicker.items.addAll(Attribute.values().map { it.value })

        sortOrderAttributePicker.valueProperty().addListener(
            ChangeListener { _, oldValue, newValue ->
                if (oldValue != newValue) {
                    controller.editSortOrder(SortOrder(Attribute.from(newValue)!!, sortOrder.isDesc), sortOrder)
                }
            }
        )

        val sortOrderLabel = Label("Sort By: ")
        sortOrderPicker.children.addAll(sortOrderLabel, sortOrderAttributePicker, sortOrderIsDescButton)
    }
}
