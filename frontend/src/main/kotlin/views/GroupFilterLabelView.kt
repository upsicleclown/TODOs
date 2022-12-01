package views

import controllers.GroupViewController
import models.Label

class GroupFilterLabelView(
    private val controller: GroupViewController,
    private val label: Label
) :
    LabelView(label) {

    override fun startEdit() {
        // Refresh the combo box options
        comboBox.items.clear()
        comboBox.items.addAll(controller.labels().map { label -> label.name })

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
        var existingLabel = controller.labels().any { l -> l.name == newLabelName }
        val newLabel = if (existingLabel) {
            controller.labels().first { l -> l.name == newLabelName }
        } else {
            var newLabel = Label(newLabelName, DEFAULT_LABEL_COLOR)
            controller.createLabel(false, newLabel)

            controller.labels().first { it.name == newLabelName }
        }

        val newFilter = controller.currentGroupProperty.value.filter.copy()
        newFilter.labelIds.remove(label.id)
        newFilter.labelIds.add(newLabel.id)
        controller.editCurrentGroupFilter(newFilter)
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
        val newFilter = controller.currentGroupProperty.value.filter.copy()
        newFilter.labelIds.remove(label.id)
        controller.editCurrentGroupFilter(newFilter)
    }
}
