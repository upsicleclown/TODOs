package commands

import client.TODOClient
import models.Group

class CreateGroupCommand(private var group: Group) : Command {
    private val todoClient = TODOClient

    override fun execute() {
        group = todoClient.createGroup(group)
    }

    override fun undo() {
        todoClient.deleteGroup(group)
    }

    override fun redo() {
        group = todoClient.createGroup(group)
    }
}
