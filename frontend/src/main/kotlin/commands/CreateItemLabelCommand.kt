package commands

import client.TODOClient
import controllers.GroupViewController
import models.Item
import models.Label

class CreateItemLabelCommand(
    private val existingLabel: Boolean,
    private val newLabel: Label,
    private val item: Item,
    private val controller: GroupViewController
) : Command {
    private val client = TODOClient()
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
        controller.reloadGroupView()
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
        controller.reloadGroupView()
    }

    override fun redo() {
        execute()
    }
}
