package views

import controllers.GroupViewController
import models.Label

class AddSettingsLabelChip(private val controller: GroupViewController) : AddLabelChip() {

    override fun startEdit() {
        // Refresh the combo box options
        addLabelComboBox.items.clear()
        addLabelComboBox.items.addAll(controller.labelListProperty.map { label -> label.name })

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

        val existingLabel = controller.labelListProperty.any { label -> label.name == newLabelName }
        val newLabel = if (existingLabel) {
            controller.labelListProperty.first { label -> label.name == newLabelName }
        } else {
            Label(newLabelName, LabelView.DEFAULT_LABEL_COLOR)
        }
        controller.createLabel(existingLabel, newLabel)

        center = addLabelButton
    }
}
