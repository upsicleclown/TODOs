package views

import controllers.SidepaneController
import models.Label

class GroupCreationLabelView(
    private val sidepaneController: SidepaneController,
    private val label: Label
) :
    LabelView(label) {

    override fun startEdit() {
        // Refresh the combo box options
        comboBox.items.clear()
        comboBox.items.addAll(sidepaneController.labelListProperty.map { label -> label.name })

        // Set the starting text of the editor to the current label name
        comboBox.editor.text = label.name

        center = comboBox
        right = null
        comboBox.requestFocus()
    }

    override fun commitEdit() {
        val newLabelName: String = if (comboBox.selectionModel.selectedItem == null) {
            comboBox.editor.text.trim()
        } else {
            comboBox.selectionModel.selectedItem
        }
        if (newLabelName.isBlank()) {
            cancelEdit()
            return
        }

        var newLabel = if (sidepaneController.groupCreationLabelListProperty.value.any { label -> label.name == newLabelName }) {
            null
        } else {
            if (sidepaneController.labelListProperty.any { l -> l.name == newLabelName }) {
                sidepaneController.labelListProperty.first { l -> l.name == newLabelName }
            } else {
                Label(newLabelName, DEFAULT_LABEL_COLOR)
            }
        }

        if (newLabel != null) {
            sidepaneController.editGroupCreationLabel(newLabel, label)
        } else {
            sidepaneController.deleteGroupCreationLabel(label)
        }

        labelText.text = label.name
        center = labelText
        right = deleteButton
    }

    override fun cancelEdit() {
        labelText.text = label.name
        center = labelText
        right = deleteButton
    }

    override fun deleteLabel() {
        sidepaneController.deleteGroupCreationLabel(label)
    }
}
