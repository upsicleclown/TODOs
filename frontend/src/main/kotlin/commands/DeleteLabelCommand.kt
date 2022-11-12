package commands

import client.TODOClient
import controllers.GroupViewController
import models.Label

/**
 * DeleteLabelCommand is used by the Settings UI to remove a label from the app
 *
 * @param label : the label that we want to delete from the app
 * @param controller : used to refresh the group view
 */
class DeleteLabelCommand(private val label: Label, private val controller: GroupViewController) : Command {
    private val client = TODOClient()

    override fun execute() {
        client.deleteLabel(label)
        controller.reloadGroupView()
    }

    /*TODO:
     * In the future, it may be worth discussing whether we want undo to also re-attach the
     * label to the items it was previously attached to. This would require some temporary storage
     * of item states, which could be done on a per-session basis. We could also just have labels store a
     * list of item IDs to make this an O(1) operation.
     */
    override fun undo() {
        client.createLabel(label)
        controller.reloadGroupView()
    }

    override fun redo() {
        execute()
    }
}
