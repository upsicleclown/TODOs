package commands

import client.TODOClient
import models.Item

class EditItemCommand(var newItem: Item, var originalItem: Item) : Command {
    private val todoClient = TODOClient()

    override fun execute() {
        todoClient.editItem(originalItem.id, newItem)
    }

    override fun undo() {
        todoClient.editItem(newItem.id, originalItem)
    }

    override fun redo() {
        todoClient.editItem(originalItem.id, newItem)
    }
}
