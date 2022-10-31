package commands

import client.TODOClient
import models.Group

class EditGroupCommand : Command {
    private val todoClient = TODOClient()
    var newGroup = Group("default")
    var originalGroup = Group("default")

    constructor(newGroup: Group, originalGroup: Group) {
        this.newGroup = newGroup
        this.originalGroup = originalGroup
    }

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
