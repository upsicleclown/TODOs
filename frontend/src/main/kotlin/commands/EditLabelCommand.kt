package commands

import client.TODOClient
import controllers.GroupViewController
import models.Label

/**
 * EditLabelCommand is used by the settings UI to replace an existing label's name
 *
 * @param collision : a boolean flag from the frontend that determines whether the newLabelName collides with
 *  an existing label
 * @param newLabelName : The name we want to update originalLabel to
 * @param originalLabel : The label that we want to update
 * @param controller : used to refresh the group view
 */
class EditLabelCommand(
    private val collision: Boolean,
    private val newLabel: Label,
    private val originalLabel: Label,
    private val controller: GroupViewController
) : Command {
    private val client = TODOClient()

    override fun execute() {
        if (collision) return
        client.editLabel(originalLabel.id, newLabel)
        controller.reloadGroupView()
    }

    override fun undo() {
        if (collision) return
        client.editLabel(originalLabel.id, originalLabel)
        controller.reloadGroupView()
    }

    override fun redo() {
        execute()
    }
}
