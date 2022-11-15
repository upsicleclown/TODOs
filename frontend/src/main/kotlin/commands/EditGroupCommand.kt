package commands

import client.TODOClient
import models.Group

class EditGroupCommand(private val newGroup: Group, private val originalGroup: Group) : Command {
    private val todoClient = TODOClient

    override fun execute() {
        todoClient.editGroup(originalGroup.id, newGroup)
    }

    override fun undo() {
        todoClient.editGroup(newGroup.id, originalGroup)
    }

    override fun redo() {
        todoClient.editGroup(originalGroup.id, newGroup)
    }
}
