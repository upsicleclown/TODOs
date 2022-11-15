package commands

import client.TODOClient
import models.Item
import models.Label

/**
 * EditItemLabelCommand is used to replace a label from an Item record with another label. The new label may or
 * may not be an existing label in our backend.
 *
 * @param existingLabel : a boolean flag from our frontend that tells us whether new label already exists in our backend
 * @param newLabel : The label we want to replace originalLabel with on the item. When existingLabel is false, this is
 *  an ad-hoc struct that needs to be saved to the backend. When existingLabel is true, this is a label record that
 *  is already saved on the backend
 * @param originalLabel : The label that we want to replace
 * @param item : the item that we want to replace a label on
 */
class EditItemLabelCommand(
    private val existingLabel: Boolean,
    private val newLabel: Label,
    private val originalLabel: Label,
    private val item: Item
) : Command {
    private val client = TODOClient
    private var newLabelRecord = newLabel

    override fun execute() {
        var newItem = item.copy()
        newItem.labelIds.remove(originalLabel.id)

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

        newItem.labelIds.add(originalLabel.id)
        client.editItem(id = item.id, newItem = newItem)
    }

    override fun redo() {
        execute()
    }
}
