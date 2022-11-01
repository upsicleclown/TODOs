package commands

import client.TODOClient
import models.Group

class CreateGroupCommand(newGroup: Group) : Command {
    private val todoClient = TODOClient()
    var group = newGroup

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
