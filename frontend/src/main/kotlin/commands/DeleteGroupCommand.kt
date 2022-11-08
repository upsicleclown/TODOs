package commands

import client.TODOClient
import controllers.SidepaneController
import models.Group

class DeleteGroupCommand(private var group: Group, private val sidepaneController: SidepaneController) : Command {
    private val todoClient = TODOClient()

    override fun execute() {
        todoClient.deleteGroup(group)
        sidepaneController.refreshGroups()
    }

    override fun undo() {
        group = todoClient.createGroup(group)
        sidepaneController.refreshGroups()
    }

    override fun redo() {
        todoClient.deleteGroup(group)
        sidepaneController.refreshGroups()
    }
}
