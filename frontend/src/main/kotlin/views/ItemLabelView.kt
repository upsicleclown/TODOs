package views

import controllers.GroupViewController
import models.Item
import models.Label

class ItemLabelView(
    private val groupController: GroupViewController,
    private val label: Label,
    private val item: Item
) :
    LabelView(label) {

    override fun startEdit() {
        // Refresh the combo box options
        comboBox.items.clear()
        comboBox.items.addAll(groupController.labels().map { label -> label.name })

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
        var existingLabel = groupController.labels().any { l -> l.name == newLabelName }
        var newLabel = if (existingLabel) {
            groupController.labels().first { l -> l.name == newLabelName }
        } else {
            Label(newLabelName, DEFAULT_LABEL_COLOR)
        }

        groupController.editItemLabel(existingLabel = existingLabel, newLabel = newLabel, originalLabel = label, item = item)
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
        groupController.deleteItemLabel(label, item)
    }
}
