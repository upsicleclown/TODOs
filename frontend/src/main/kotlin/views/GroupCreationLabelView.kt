package views

import controllers.SidepaneController
import models.Group
import models.Label

class GroupCreationLabelView(
    private val sidepaneController: SidepaneController,
    private val label: Label,
    private val group: Group
) :
    LabelView(label) {

    init {
        comboBox.isEditable = false
    }

    override fun startEdit() {
        // Refresh the combo box options
        comboBox.items.clear()
        comboBox.items.addAll(sidepaneController.labels().map { label -> label.name })

        // Set the starting text of the editor to the current label name
        comboBox.editor.text = label.name

        center = comboBox
        right = null
        comboBox.requestFocus()
    }

    override fun commitEdit() {
        val newLabelName = if (comboBox.selectionModel.selectedItem == null) {
            cancelEdit()
            return
        } else {
            comboBox.selectionModel.selectedItem
        }
        val existingLabel = sidepaneController.labels().first { label -> label.name == newLabelName }
        group.filter.labelIds.add(existingLabel.id)

        center = labelText
        right = deleteButton

        sidepaneController.reloadGroupCreationView()
    }

    override fun cancelEdit() {
        labelText.text = label.name
        center = labelText
        right = deleteButton
    }
    override fun deleteLabel() {
        group.filter.labelIds.remove(label.id)
        sidepaneController.reloadGroupCreationView()
    }
}
