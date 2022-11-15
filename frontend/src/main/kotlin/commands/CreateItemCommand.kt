package commands

import client.TODOClient
import models.Item

class CreateItemCommand(private var item: Item) : Command {
    private val todoClient = TODOClient

    override fun execute() {
        item = todoClient.createItem(item)
    }

    override fun undo() {
        todoClient.deleteItem(item)
    }

    override fun redo() {
        execute()
    }
}
