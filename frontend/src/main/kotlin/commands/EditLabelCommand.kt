package commands

import client.TODOClient
import controllers.GroupViewController
import models.Label

/**
 * This command should only be used by the Settings UI for managing labels.
 */
class EditLabelCommand(
    private val collision: Boolean,
    private val newLabelName: String,
    private val originalLabel: Label,
    private val controller: GroupViewController
) : Command {
    private val client = TODOClient()

    override fun execute() {
        if (collision) return
        client.editLabel(originalLabel.id, newLabelName)
        controller.reloadGroupView()
    }

    override fun undo() {
        if (collision) return
        client.editLabel(originalLabel.id, originalLabel.name)
        controller.reloadGroupView()
    }

    override fun redo() {
        execute()
    }
}
