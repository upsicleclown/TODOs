package views

import controllers.GroupViewController
import controllers.SidepaneController
import javafx.event.EventHandler
import javafx.scene.control.Button
import javafx.scene.input.MouseEvent
import javafx.scene.layout.Priority
import javafx.scene.layout.VBox

class SidepaneView(
    private val sidepaneController: SidepaneController,
    private val groupViewController: GroupViewController
) : VBox() {

    // TODO: At some point, this should be wrapped in a ScrollContainer.
    //  But I have PTSD so I'm not going to do this now.
    private var groupListContainer = VBox(24.0)
    private val openGroupCreationDialogButton = Button("+ group")
    private val groupCreationDialog = GroupCreationView()
    private val SIDEPANE_WIDTH = 200.0

    init {
        /* region styling */
        styleClass.add("sidepane")
        setVgrow(groupListContainer, Priority.ALWAYS)
        prefWidth = SIDEPANE_WIDTH
        groupListContainer.styleClass.add("sidepane__list")
        openGroupCreationDialogButton.styleClass.addAll("body", "sidepane__add-group")
        /* end region styling */

        /* region event filters */
        // When clicking outside the group, remove focus from the group
        addEventHandler(
            MouseEvent.MOUSE_CLICKED,
            EventHandler<MouseEvent> { requestFocus() }
        )
        /* end region event filters */

        // Field to create groups
        openGroupCreationDialogButton.onAction = EventHandler {
            val optionalCreatedGroup = groupCreationDialog.showAndWait()
            if (optionalCreatedGroup.isPresent) {
                sidepaneController.createGroup(optionalCreatedGroup.get())
            }
        }
        children.addAll(groupListContainer, openGroupCreationDialogButton)
    }

    fun refreshGroups() {
        groupListContainer.children.clear()
        sidepaneController.loadGroups()
        groupListContainer.children.addAll(
            sidepaneController.groups().map {
                    group ->
                SidepaneGroup(sidepaneController = sidepaneController, group = group)
            }
        )

        // After updating a SidepaneGroup, refresh the GroupView to reflect title
        groupViewController.reloadGroupView()
    }
}
