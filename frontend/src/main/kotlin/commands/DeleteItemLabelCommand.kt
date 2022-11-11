package commands

import controllers.GroupViewController
import models.Item
import models.Label

class DeleteItemLabelCommand(
    private val label: Label,
    private val item: Item,
    private val controller: GroupViewController
) : Command {
    override fun execute() {
        var newItem = item.copy()
        newItem.labelIds.remove(label.id)

        controller.editItem(newItem = newItem, originalItem = item)
        controller.reloadGroupView()
    }

    /**
     * Note: if DeleteItemLabelCommand.undo is called on an item that never had the referenced label,
     * it will add the label. This is another reason we should track item state somewhere
     */
    override fun undo() {
        var newItem = item.copy()
        newItem.labelIds.add(label.id)

        controller.editItem(newItem = newItem, originalItem = item)
        controller.reloadGroupView()
    }

    override fun redo() {
        execute()
    }
}
