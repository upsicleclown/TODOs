package commands

import client.TODOClient
import models.Item

class DeleteItemCommand(var item: Item) : Command {
    private val todoClient = TODOClient()

    override fun execute() {
        todoClient.deleteItem(item)
    }

    override fun undo() {
        todoClient.createItem(item)
    }

    override fun redo() {
        todoClient.deleteItem(item)
    }
}
