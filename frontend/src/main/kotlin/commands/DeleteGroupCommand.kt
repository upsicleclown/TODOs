package commands

import client.TODOClient
import models.Group

class DeleteGroupCommand(private val group: Group) : Command {
    private val todoClient = TODOClient()

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
