package commands

import client.TODOClient
import controllers.GroupViewController
import models.Label

class CreateLabelCommand(private val label: Label, private val controller: GroupViewController) : Command {
    private val client = TODOClient()

    override fun execute() {
        client.createLabel(label)
        controller.reloadGroupView()
    }

    override fun undo() {
        client.deleteLabel(label)
        controller.reloadGroupView()
    }

    override fun redo() {
        client.createLabel(label)
        controller.reloadGroupView()
    }
}
