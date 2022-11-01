package commands

import client.TODOClient
import models.Group

class CreateGroupCommand(private val group: Group) : Command {
    private val todoClient = TODOClient()

    override fun execute() {
        todoClient.createGroup(group)
    }

    override fun undo() {
        todoClient.deleteGroup(group)
    }

    override fun redo() {
        todoClient.createGroup(group)
    }
}
