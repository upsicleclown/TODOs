package commands

import client.TODOClient
import controllers.GroupViewController
import models.Item
import models.Label

/**
 * DeleteItemLabelCommand is used to remove a label from an Item record.
 *
 * @param label : The label we want to delete from the item
 * @param item : The item we want to remove a label from
 * @param controller : used to reload the group view
 */
class DeleteItemLabelCommand(
    private val label: Label,
    private val item: Item,
    private val controller: GroupViewController
) : Command {
    private val client = TODOClient()

    override fun execute() {
        var newItem = item.copy()
        newItem.labelIds.remove(label.id)

        client.editItem(id = item.id, newItem = newItem)
        controller.reloadGroupView()
    }

    override fun undo() {
        // TODO: We need to add item state management to handle undos when the item never had the label
        var newItem = item.copy()
        newItem.labelIds.add(label.id)

        client.editItem(id = item.id, newItem = newItem)
        controller.reloadGroupView()
    }

    override fun redo() {
        execute()
    }
}
