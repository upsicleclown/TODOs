package commands

import controllers.GroupViewController
import models.Label

class EditLabelCommand(private val label: Label, private val controller: GroupViewController) : Command {
    override fun execute() {
        TODO("Not yet implemented")
    }

    override fun undo() {
        TODO("Not yet implemented")
    }

    override fun redo() {
        TODO("Not yet implemented")
    }
}
