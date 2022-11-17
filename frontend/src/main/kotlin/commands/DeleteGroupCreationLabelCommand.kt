package commands

import client.TODOClient
import controllers.SidepaneController
import models.Label

/**
 * DeleteGroupCreationLabelCommand is used to remove a label created in a GroupCreationView.
 *
 * @param label : The label we want to delete from the item
 * @param controller: The controller that controls the GroupCreationView
 */
class DeleteGroupCreationLabelCommand(
    private val label: Label,
    private val controller: SidepaneController
) : Command {
    private val client = TODOClient

    override fun execute() {
        controller.groupCreationLabelListProperty.remove(label)
    }

    override fun undo() {
        controller.groupCreationLabelListProperty.add(label)
    }

    override fun redo() {
        execute()
    }
}
