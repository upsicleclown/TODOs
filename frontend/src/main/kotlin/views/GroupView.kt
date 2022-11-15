package views

import controllers.GroupViewController
import javafx.event.EventHandler
import javafx.scene.control.Label
import javafx.scene.control.ScrollPane
import javafx.scene.control.TextField
import javafx.scene.input.MouseEvent
import javafx.scene.layout.VBox
import models.Item

class GroupView(private val controller: GroupViewController) : VBox(36.0) {

    private val itemListScrollContainer = ScrollPane()
    private val itemListContainer = VBox(36.0)
    private val itemCreationField = TextField()
    private var currentGroupName = Label("")

    init {
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

        children.addAll(currentGroupName, itemCreationField, itemListScrollContainer)
        /* end region view setup */

        /* region data bindings */
        controller.currentGroupProperty.addListener { _, _, newGroup ->
            currentGroupName.text = newGroup.name
        }

        controller.itemListProperty.addListener { _, _, newItemList ->
            itemListContainer.children.setAll(
                newItemList.map {
                        item ->
                    ItemView(controller, item)
                }
            )
        }
        /* end region data bindings */
    }
}
