package commands

import client.TODOClient
import models.Item

class CreateItemCommand : Command {
    private val todoClient = TODOClient()
    var item: Item = Item("default", false)

    constructor(itemTitle: String) {
        this.item = Item(itemTitle, false)
    }

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
