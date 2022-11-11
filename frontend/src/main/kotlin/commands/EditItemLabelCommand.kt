package commands

import controllers.GroupViewController
import models.Item
import models.Label

class EditItemLabelCommand(private val existingLabel: Boolean, private val newLabel: Label,
                           private val originalLabel: Label, private val item: Item,
                           private val controller: GroupViewController) : Command {
    override fun execute() {
        var newItem = item.copy()
        newItem.labelIds.remove(originalLabel.id)

        if (existingLabel) {
            newItem.labelIds.add(newLabel.id)
        } else {
            val newLabelRecord = controller.createLabel(newLabel)
            newItem.labelIds.add(newLabelRecord.id)
        }

        controller.editItem(newItem=newItem, originalItem=item)
        controller.reloadGroupView()
    }

    override fun undo() {
        var newItem = item.copy()

        if (existingLabel) {
            newItem.labelIds.remove(newLabel.id)
        } else {
            // newLabel is an object created ad-hoc. newLabel.id doesn't necessarily math the id of the
            // label actually created on the backend, so we much search for this label
            var newLabelRecord = controller.labels().last { label: Label ->  label.name == newLabel.name }
            newItem.labelIds.remove(newLabelRecord.id)
        }


        newItem.labelIds.add(originalLabel.id)
        controller.editItem(newItem=newItem, originalItem=item)
        controller.reloadGroupView()
    }

    override fun redo() {
        execute()
    }
}
