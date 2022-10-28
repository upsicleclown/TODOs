package commands

import client.TODOClient
import models.Item

class EditItemCommand : Command {
    private val todoClient = TODOClient()
    var newItem = Item("default", false)
    var originalItem = Item("default", false)

    constructor(newItem: Item, originalItem: Item) {
        this.newItem = newItem
        this.originalItem = originalItem
    }

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
