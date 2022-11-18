package commands

import client.TODOClient
import models.Group
import models.Item

class CreateItemCommand(private var item: Item, private var currentGroup: Group?) : Command {
    private val todoClient = TODOClient

    override fun execute() {
        currentGroup?.let { group ->
            if (group.filter.priorities.size != 0) item.priority = group.filter.priorities[0]
            group.filter.edtStartDateRange?.let { item.edtDueDate = group.filter.edtStartDateRange }
            group.filter.isCompleted?.let { item.isCompleted = group.filter.isCompleted!! }
            item.labelIds.addAll(group.filter.labelIds)
        }
        item = todoClient.createItem(item)
    }

    override fun undo() {
        todoClient.deleteItem(item)
    }

    override fun redo() {
        execute()
    }
}
