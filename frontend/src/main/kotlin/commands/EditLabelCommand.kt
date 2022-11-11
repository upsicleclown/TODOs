package commands

import client.TODOClient
import controllers.GroupViewController
import models.Label

/**
 * This command should only be used by the Settings UI for managing labels.
 */
class EditLabelCommand(
    private val newLabel: Label,
    private val originalLabel: Label,
    private val controller: GroupViewController
) : Command {
    private val client = TODOClient()

    override fun execute() {
        client.editLabel(originalLabel.id, newLabel)
        controller.reloadGroupView()
    }

    override fun undo() {
        client.editLabel(newLabel.id, originalLabel)
        controller.reloadGroupView()
    }

    override fun redo() {
        execute()
    }
}
