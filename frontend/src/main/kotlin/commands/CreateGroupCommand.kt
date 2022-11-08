package commands

import client.TODOClient
import controllers.SidepaneController
import models.Group

class CreateGroupCommand(private var group: Group, private val sidepaneController: SidepaneController) : Command {
    private val todoClient = TODOClient()

    override fun execute() {
        group = todoClient.createGroup(group)
        sidepaneController.refreshGroups()
    }

    override fun undo() {
        todoClient.deleteGroup(group)
        sidepaneController.refreshGroups()
    }

    override fun redo() {
        group = todoClient.createGroup(group)
        sidepaneController.refreshGroups()
    }
}
