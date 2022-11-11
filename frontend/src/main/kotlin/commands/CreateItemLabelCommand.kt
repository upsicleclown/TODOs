package commands

import controllers.GroupViewController
import models.Label

/* should probably use editItem for all of these */
class CreateItemLabelCommand(private val label: Label, private val controller: GroupViewController) : Command {
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
