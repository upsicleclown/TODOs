package commands

import client.TODOClient
import controllers.GroupViewController
import models.Group

class EditGroupFilterCommand(private val newGroup: Group, private val originalGroup: Group, private val controller: GroupViewController) : Command {
    private val todoClient = TODOClient

    override fun execute() {
        todoClient.editGroup(controller.currentGroupProperty.value.id, newGroup)
        controller.reloadCurrentGroup(newGroup)
    }

    override fun undo() {
        todoClient.editGroup(controller.currentGroupProperty.value.id, originalGroup)
        controller.reloadCurrentGroup(originalGroup)
    }

    override fun redo() {
        execute()
    }
}
