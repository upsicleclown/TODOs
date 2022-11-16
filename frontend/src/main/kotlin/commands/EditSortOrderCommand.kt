package commands

import controllers.GroupViewController
import views.GroupView

class EditSortOrderCommand(private val newSortOrder: GroupView.SortOrder, private val originalSortOrder: GroupView.SortOrder, private val controller: GroupViewController) : Command {

    override fun execute() {
        controller.setSortOrder(newSortOrder)
    }

    override fun undo() {
        controller.setSortOrder(originalSortOrder)
    }

    override fun redo() {
        controller.setSortOrder(newSortOrder)
    }
}
