package commands

import client.TODOClient
import controllers.GroupViewController
import models.Item

class DeleteItemCommand(private var item: Item, private val groupViewController: GroupViewController) : Command {
    private val todoClient = TODOClient()

    override fun execute() {
        todoClient.deleteItem(item)
        groupViewController.reloadGroupView()
    }

    override fun undo() {
        item = todoClient.createItem(item)
        groupViewController.reloadGroupView()
    }

    override fun redo() {
        todoClient.deleteItem(item)
        groupViewController.reloadGroupView()
    }
}
