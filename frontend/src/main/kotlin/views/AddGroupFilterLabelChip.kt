package views

import controllers.GroupViewController
import models.Label

class AddGroupFilterLabelChip(private val controller: GroupViewController) : AddLabelChip() {

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

        var existingLabel = controller.labels().any { l -> l.name == newLabelName }
        val label = if (existingLabel) {
            controller.labels().first { l -> l.name == newLabelName }
        } else {
            var newLabel = Label(newLabelName, LabelView.DEFAULT_LABEL_COLOR)
            controller.createLabel(false, newLabel)

            controller.labels().first { it.name == newLabelName }
        }

        val newFilter = controller.currentGroupProperty.value.filter.copy()
        newFilter.labelIds.add(label.id)
        controller.editCurrentGroupFilter(newFilter)
        center = addLabelButton
    }
}
