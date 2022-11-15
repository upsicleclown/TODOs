package commands

import client.TODOClient
import models.Item
import models.Label

/**
 * CreateItemLabelCommand is used to add a Label to an Item record.
 *
 * @param existingLabel : a Boolean flag from the frontend that tells us
 *  whether a user typed in a new label name or chose an existing one from the combo box
 * @param newLabel : the Label object that the user wants to add to the Item.
 *  When existingLabel is false, this is an ad-hoc Label struct that needs to be saved to the backend.
 *  When existingLabel is true, this is a valid Label record stored on the backend
 * @param item : the Item record which we want to add the Label to
 */
class CreateItemLabelCommand(
    private val existingLabel: Boolean,
    private val newLabel: Label,
    private val item: Item
) : Command {
    private val client = TODOClient
    private var newLabelRecord = newLabel

    override fun execute() {
        var newItem = item.copy()

        if (existingLabel) {
            newItem.labelIds.add(newLabel.id)
        } else {
            newLabelRecord = client.createLabel(newLabel)
            newItem.labelIds.add(newLabelRecord.id)
        }

        client.editItem(id = item.id, newItem = newItem)
    }

    override fun undo() {
        var newItem = item.copy()

        if (existingLabel) {
            newItem.labelIds.remove(newLabel.id)
        } else {
            client.deleteLabel(newLabelRecord)
            newItem.labelIds.remove(newLabelRecord.id)
        }

        client.editItem(id = item.id, newItem = newItem)
    }

    override fun redo() {
        execute()
    }
}
