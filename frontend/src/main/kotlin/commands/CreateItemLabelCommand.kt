package commands

import controllers.GroupViewController
import models.Item
import models.Label

class CreateItemLabelCommand(
    private val label: Label,
    private val item: Item,
    private val controller: GroupViewController
) : Command {
    override fun execute() {
        var newItem = item.copy()
        newItem.labelIds.add(label.id)

        controller.editItem(newItem = newItem, originalItem = item)
        controller.reloadGroupView()
    }

    override fun undo() {
        var newItem = item.copy()
        newItem.labelIds.remove(label.id)

        controller.editItem(newItem = newItem, originalItem = item)
        controller.reloadGroupView()
    }

    override fun redo() {
        execute()
    }
}
