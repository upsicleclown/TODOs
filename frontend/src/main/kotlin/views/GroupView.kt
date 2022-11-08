package views

import controllers.GroupViewController
import javafx.event.EventHandler
import javafx.scene.control.Label
import javafx.scene.control.ScrollPane
import javafx.scene.control.TextField
import javafx.scene.input.MouseEvent
import javafx.scene.layout.VBox
import models.Group
import models.Item

class GroupView(private val controller: GroupViewController) : VBox(36.0) {

    private var currentGroupName = Label("")
    private val itemListScrollContainer = ScrollPane()
    private val itemListContainer = VBox(36.0)
    private val itemCreationField = TextField()
    private var groupItemModels = listOf<Item>()

    init {
        /* region styling */
        styleClass.add("group")
        currentGroupName.styleClass.addAll("group__title", "title-max")
        itemCreationField.styleClass.addAll("h2", "group__create-item")
        itemListScrollContainer.styleClass.addAll("group__item-list__container")
        itemListContainer.styleClass.addAll("group__item-list")
        /* end region */

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
        /* end region */

        /* region view setup */
        itemCreationField.promptText = "Create a new item..."

        itemListScrollContainer.isFitToWidth = true
        itemListScrollContainer.content = itemListContainer
        itemListScrollContainer.hmax = 0.0
        itemListScrollContainer.hbarPolicy = ScrollPane.ScrollBarPolicy.NEVER

        itemListContainer.children.addAll(
            groupItemModels.map { item -> ItemView(controller, item) }
        )

        children.addAll(currentGroupName, itemCreationField, itemListScrollContainer)
        /* end region */
    }

    fun refreshWithItems(group: Group?, items: List<Item>) {
        itemListContainer.children.clear()
        currentGroupName.text = group?.name
        children[0] = currentGroupName
        groupItemModels = items
        itemListContainer.children.addAll(
            groupItemModels.map { item -> ItemView(controller, item) }
        )
    }
}
