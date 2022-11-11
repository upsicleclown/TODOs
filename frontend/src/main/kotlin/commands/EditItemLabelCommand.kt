package commands

import controllers.GroupViewController
import models.Label

class EditItemLabelCommand(private val label: Label, private val controller: GroupViewController) : Command {
    override fun execute() {
    }

    override fun undo() {
        TODO("Not yet implemented")
    }

    override fun redo() {
        TODO("Not yet implemented")
    }
}
