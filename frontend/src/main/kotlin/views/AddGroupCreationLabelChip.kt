package views

import controllers.SidepaneController
import models.Group

class AddGroupCreationLabelChip(private val controller: SidepaneController, private val group: Group) : AddLabelChip() {

    init {
        addLabelComboBox.isEditable = false
    }

    override fun startEdit() {
        // Refresh the combo box options
        addLabelComboBox.items.clear()
        addLabelComboBox.items.addAll(controller.labels().map { label -> label.name })

        center = addLabelComboBox
        addLabelComboBox.requestFocus()
    }

    override fun commitEdit() {
        val newLabelName = if (addLabelComboBox.selectionModel.selectedItem == null) {
            cancelEdit()
            return
        } else {
            addLabelComboBox.selectionModel.selectedItem
        }
        val existingLabel = controller.labels().first { label -> label.name == newLabelName }
        group.filter.labelIds.add(existingLabel.id)

        controller.reloadGroupCreationView()

        center = addLabelButton
    }
}
