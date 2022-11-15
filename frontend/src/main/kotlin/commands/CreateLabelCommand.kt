package commands

import client.TODOClient
import models.Label

/**
 * CreateLabelCommand is used by the Settings UI to add a new Label record to the backend for
 * later use by different items.
 *
 * @param collision : A boolean flag from the frontend that determines whether there
 *  is a naming collision with a current label
 * @param newLabel : An ad-hoc class representing the label that we want to create
 */
class CreateLabelCommand(
    private val collision: Boolean,
    private val newLabel: Label
) : Command {
    private val client = TODOClient

    override fun execute() {
        // If label already exists, don't create it
        if (collision) return

        client.createLabel(newLabel)
    }

    override fun undo() {
        // If label wasn't created because it was a duplicate, then don't delete it
        if (collision) return

        client.deleteLabel(newLabel)
    }

    override fun redo() {
        execute()
    }
}
