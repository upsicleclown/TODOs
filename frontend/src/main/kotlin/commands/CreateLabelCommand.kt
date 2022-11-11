package commands

import client.TODOClient
import controllers.GroupViewController
import models.Label

class CreateLabelCommand(private val existing: Boolean, private val newLabel: Label,
                         private val controller: GroupViewController) : Command {
    private val client = TODOClient()
    private var existingLabel = false

    override fun execute() {
        // If label already exists, don't create it
        if (existing) return

        client.createLabel(newLabel)
        controller.reloadGroupView()
    }

    override fun undo() {
        // If label wasn't created because it was a duplicate, then don't delete it
        if (existing) return

        client.deleteLabel(newLabel)
        controller.reloadGroupView()
    }

    override fun redo() {
        execute()
    }
}
