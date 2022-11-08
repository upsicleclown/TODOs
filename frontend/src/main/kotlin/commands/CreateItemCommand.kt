package commands

import client.TODOClient
import controllers.GroupViewController
import models.Item

class CreateItemCommand(private var item: Item, private val groupViewController: GroupViewController) : Command {
    private val todoClient = TODOClient()

    override fun execute() {
        item = todoClient.createItem(item)
        groupViewController.reloadGroupView()
    }

    override fun undo() {
        todoClient.deleteItem(item)
        groupViewController.reloadGroupView()
    }

    override fun redo() {
        item = todoClient.createItem(item)
        groupViewController.reloadGroupView()
    }
}
