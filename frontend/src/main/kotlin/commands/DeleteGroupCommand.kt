package commands

import client.TODOClient
import models.Group

class DeleteGroupCommand : Command {
    private val todoClient = TODOClient()
    var group = Group("default")

    constructor(group: Group) {
        this.group = group
    }

    override fun execute() {
        todoClient.deleteGroup(group)
    }

    override fun undo() {
        todoClient.createGroup(group)
    }

    override fun redo() {
        todoClient.deleteGroup(group)
    }
}
