package views

import controllers.SidepaneController
import models.Label

class AddGroupCreationLabelChip(private val controller: SidepaneController) : AddLabelChip() {

    override fun startEdit() {
        // Refresh the combo box options
        addLabelComboBox.items.clear()
        addLabelComboBox.items.setAll(controller.labelListProperty.map { label -> label.name })

        center = addLabelComboBox
        addLabelComboBox.requestFocus()
    }

    override fun commitEdit() {
        val newLabelName = if (addLabelComboBox.selectionModel.selectedItem == null) {
            addLabelComboBox.editor.text.trim()
        } else {
            addLabelComboBox.selectionModel.selectedItem
        }
        if (newLabelName.isBlank()) {
            cancelEdit()
            return
        }

        var newLabel = if (controller.groupCreationLabelListProperty.value.any { label -> label.name == newLabelName }) {
            null
        } else {
            if (controller.labelListProperty.any { label -> label.name == newLabelName }) {
                controller.labelListProperty.first { label -> label.name == newLabelName }
            } else {
                Label(newLabelName, LabelView.DEFAULT_LABEL_COLOR)
            }
        }
        newLabel?.let { controller.createGroupCreationLabel(it) }

        center = addLabelButton
    }
}
