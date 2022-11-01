package commands

import client.TODOClient
import models.Item

class CreateItemCommand(private val item: Item) : Command {
    private val todoClient = TODOClient()

    override fun execute() {
        todoClient.createItem(item)
    }

    override fun undo() {
        todoClient.deleteItem(item)
    }

    override fun redo() {
        todoClient.createItem(item)
    }
}
