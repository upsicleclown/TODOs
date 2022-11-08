package commands

import client.TODOClient
import controllers.GroupViewController
import models.Item

class EditItemCommand(private var newItem: Item, private var originalItem: Item, private val groupViewController: GroupViewController) : Command {
    private val todoClient = TODOClient()

    override fun execute() {
        todoClient.editItem(originalItem.id, newItem)
        groupViewController.reloadGroupView()
    }

    override fun undo() {
        todoClient.editItem(newItem.id, originalItem)
        groupViewController.reloadGroupView()
    }

    override fun redo() {
        todoClient.editItem(originalItem.id, newItem)
        groupViewController.reloadGroupView()
    }
}
