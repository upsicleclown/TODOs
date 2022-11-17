package commands

import controllers.SidepaneController
import models.Label

/**
 * EditGroupCreationLabelCommand is used to replace a label from a GroupCreationView with another label
 *
 * @param newLabel : The label we want to replace originalLabel with.
 * @param originalLabel : The label that we want to replace
 * @param controller: The controller is included here since it
 * owns the groupCreationLabelListPropery that shores the labels being used in group creation
 */
class EditGroupCreationLabelCommand(
    private val newLabel: Label,
    private val originalLabel: Label,
    private val controller: SidepaneController
) : Command {

    override fun execute() {
        controller.groupCreationLabelListProperty.remove(originalLabel)
        controller.groupCreationLabelListProperty.add(newLabel)
    }

    override fun undo() {
        controller.groupCreationLabelListProperty.remove(newLabel)
        controller.groupCreationLabelListProperty.add(originalLabel)
    }

    override fun redo() {
        execute()
    }
}
