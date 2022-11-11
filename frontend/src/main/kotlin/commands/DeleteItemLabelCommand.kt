package commands

import controllers.GroupViewController
import models.Label

/* should probably use editItem for all of these */
class DeleteItemLabelCommand(private val label: Label, controller: GroupViewController) : Command {
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
