package commands

import client.TODOClient
import models.Item

class CreateItemCommand(newItem: Item) : Command {
    private val todoClient = TODOClient()
    var item: Item = newItem

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
