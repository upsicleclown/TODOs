package commands

import client.TODOClient
import models.Item

class EditItemCommand : Command {
    private val todoClient = TODOClient()
    var item: Item = Item("default", false)

    constructor(item: Item) {
        this.item = item
    }

    override fun execute() {
        todoClient.editItem(item)
    }

    override fun undo() {
    }

    override fun redo() {
    }
}
