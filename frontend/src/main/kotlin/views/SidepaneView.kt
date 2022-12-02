package views

import cache.Cache
import controllers.SidepaneController
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.scene.control.Button
import javafx.scene.control.ScrollPane
import javafx.scene.input.MouseEvent
import javafx.scene.layout.Priority
import javafx.scene.layout.VBox
import models.Group
import java.util.Optional

class SidepaneView(
    private val sidepaneController: SidepaneController,
    private val cache: Cache
) : VBox(24.0) {

    private var groupListContainer = VBox(24.0)
    private var groupListScrollContainer = ScrollPane()
    var groupCreationDialog: GroupCreationView? = null
    private val openGroupCreationDialogButton = Button("+ group")
    private val SIDEPANE_WIDTH = 200.0
    private val showAllItemsButton = Button("all")

    init {
        /* region styling */
        styleClass.add("sidepane")
        prefWidth = SIDEPANE_WIDTH
        setVgrow(groupListScrollContainer, Priority.ALWAYS)
        groupListScrollContainer.styleClass.add("sidepane__list__container")
        groupListContainer.styleClass.add("sidepane__list")
        openGroupCreationDialogButton.styleClass.addAll("body", "sidepane__add-group")
        showAllItemsButton.styleClass.addAll("body", "sidepane__default-group")
        /* end region styling */

        /* region event filters */
        // When clicking outside the group, remove focus from the group
        addEventHandler(
            MouseEvent.MOUSE_CLICKED,
            EventHandler<MouseEvent> { requestFocus() }
        )
        /* end region event filters */

        /* region view setup */
        groupListScrollContainer.content = groupListContainer
        groupListScrollContainer.isFitToWidth = true
        groupListScrollContainer.hbarPolicy = ScrollPane.ScrollBarPolicy.NEVER
        groupListScrollContainer.hmax = 0.0

        // Field to create groups
        openGroupCreationDialogButton.onAction = EventHandler {

            groupCreationDialog = GroupCreationView(sidepaneController, cache)
            val createdGroup: Optional<Group?> = groupCreationDialog!!.showAndWait()
            if (createdGroup.isPresent) {
                sidepaneController.createGroup(createdGroup.get())
            }
        }
        /* end region view setup */

        /* region data bindings */
        showAllItemsButton.onAction = EventHandler<ActionEvent?> { _ ->
            sidepaneController.focusGroup(null)
        }

        sidepaneController.groupListProperty.addListener { _, _, newList ->
            groupListContainer.children.setAll(
                newList.map {
                        group ->
                    SidepaneGroup(sidepaneController, group)
                }
            )
        }
        /* end region data bindings */
        children.addAll(showAllItemsButton, groupListScrollContainer, openGroupCreationDialogButton)
    }
}
