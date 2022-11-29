package commands

import client.TODOClient
import models.Label

/**
 * EditLabelCommand is used by the settings UI to replace an existing label's name
 *
 * @param collision : a boolean flag from the frontend that determines whether the newLabelName collides with
 *  an existing label
 * @param newLabel : The label data we want to update originalLabel to
 * @param originalLabel : The label that we want to update
 */
class EditLabelCommand(
    private val collision: Boolean,
    private val newLabel: Label,
    private val originalLabel: Label
) : Command {
    private val client = TODOClient

    override fun execute() {
        if (collision) return
        newLabel.id = originalLabel.id
        client.editLabel(originalLabel.id, newLabel)
    }

    override fun undo() {
        if (collision) return
        client.editLabel(originalLabel.id, originalLabel)
    }

    override fun redo() {
        execute()
    }
}
