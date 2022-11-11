package commands

import controllers.GroupViewController
import models.Item
import models.Label

class CreateItemLabelCommand(
    private val existingLabel: Boolean,
    private val newLabel: Label,
    private val item: Item,
    private val controller: GroupViewController
) : Command {
    override fun execute() {
        var newItem = item.copy()

        if (existingLabel) {
            newItem.labelIds.add(newLabel.id)
        } else {
            controller.createLabel(newLabel)
            var newLabelRecord = controller.labels().last { label -> label.name == newLabel.name }
            newItem.labelIds.add(newLabelRecord.id)
        }

        controller.editItem(newItem = newItem, originalItem = item)
        controller.reloadGroupView()
    }

    override fun undo() {
        var newItem = item.copy()

        if (existingLabel) {
            newItem.labelIds.remove(newLabel.id)
        } else {
            var newLabelRecord = controller.labels().last { label -> label.name == newLabel.name }
            newItem.labelIds.remove(newLabel.id)
        }

        controller.editItem(newItem = newItem, originalItem = item)
        controller.reloadGroupView()
    }

    override fun redo() {
        execute()
    }
}
