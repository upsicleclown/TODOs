package commands

import controllers.SidepaneController
import models.Label

/**
 * CreateGroupCreationLabelCommand is used to create a Label from a GroupCreationView.
 *
 * @param newLabel : the Label object that the user wants to add to the Item.
 *  When existingLabel is false, this is an ad-hoc Label struct that needs to be saved to the backend.
 *  When existingLabel is true, this is a valid Label record stored on the backend
 * @param controller : the controller of the GroupCreationView. The controller is included here since it
 * owns the groupCreationLabelListPropery that shores the labels being used in group creation
 */
class CreateGroupCreationLabelCommand(
    private val newLabel: Label,
    private val controller: SidepaneController
) : Command {

    override fun execute() {
        controller.groupCreationLabelListProperty.add(newLabel)
    }

    override fun undo() {
        controller.groupCreationLabelListProperty.remove(newLabel)
    }

    override fun redo() {
        execute()
    }
}
