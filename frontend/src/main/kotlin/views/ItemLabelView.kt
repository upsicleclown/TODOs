package views

import controllers.GroupViewController
import models.Item
import models.Label

class ItemLabelView(
    private val groupController: GroupViewController,
    private val label: Label,
    private val item: Item
) :
    LabelView(groupController, label, item) {
    override fun startEdit() {
        center = textField
        right = null

        textField.requestFocus()
    }

    override fun commitEdit(newLabelName: String) {
        var existingLabel = groupController.labels().any { l -> l.name == newLabelName }
        var newLabel = if (existingLabel) {
            groupController.labels().first { l -> l.name == newLabelName }
        } else {
            Label(newLabelName, LabelView.DEFAULT_LABEL_COLOR)
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
}
