package commands

import client.TODOClient
import controllers.SidepaneController
import models.Group

class EditGroupCommand(private val newGroup: Group, private val originalGroup: Group, private val sidepaneController: SidepaneController) : Command {
    private val todoClient = TODOClient()

    override fun execute() {
        todoClient.editGroup(originalGroup.id, newGroup)
        sidepaneController.refreshGroups()
    }

    override fun undo() {
        todoClient.editGroup(newGroup.id, originalGroup)
        sidepaneController.refreshGroups()
    }

    override fun redo() {
        todoClient.editGroup(originalGroup.id, newGroup)
        sidepaneController.refreshGroups()
    }
}
