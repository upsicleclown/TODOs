package commands

import client.TODOClient
import controllers.GroupViewController
import models.Label

class CreateLabelCommand(private val newLabel: Label, private val controller: GroupViewController) : Command {
    private val client = TODOClient()

    override fun execute() {
        // If label already exists, don't create it
        var existingLabel = controller.labels().any { label -> label.name == newLabel.name }
        if (existingLabel) return

        client.createLabel(newLabel)
        controller.reloadGroupView()
    }

    override fun undo() {
        client.deleteLabel(newLabel)
        controller.reloadGroupView()
    }

    override fun redo() {
        execute()
    }
}
