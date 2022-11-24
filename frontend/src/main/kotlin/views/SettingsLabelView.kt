package views

import controllers.GroupViewController
import javafx.scene.control.Alert
import javafx.scene.control.Alert.AlertType
import javafx.scene.control.ButtonType
import javafx.scene.control.ColorPicker
import javafx.scene.layout.HBox
import javafx.scene.paint.Color
import models.Label
import java.util.*

class SettingsLabelView(
    private val groupController: GroupViewController,
    private val label: Label
) : LabelView(label) {
    private val colorPicker = ColorPicker(Color.web(label.color))

    init {
        styleClass.add("settings__label")
        labelText.styleClass.addAll("settings__label-text", "body")
        colorPicker.styleClass.addAll("label-max", "settings__color-picker")

        colorPicker.setOnAction {
            val originalLabel = label.copy()
            val colorHexValue = "#" + colorPicker.value.toString().substring(2, 8)
            label.color = colorHexValue
            groupController.editLabel(false, newLabel = label, originalLabel = originalLabel)
        }

        top = null
        center = labelText
        right = HBox(colorPicker, deleteButton)
        left = null
    }

    override fun startEdit() {
        // Refresh the combo box options
        comboBox.items.clear()
        comboBox.items.addAll(groupController.labelListProperty.map { label -> label.name })

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

        var newLabel = if (groupController.labelListProperty.value.any { label -> label.name == newLabelName }) {
            null
        } else {
            if (groupController.labelListProperty.any { label -> label.name == newLabelName }) {
                groupController.labelListProperty.first { label -> label.name == newLabelName }
            } else {
                Label(newLabelName, label.color)
            }
        }

        if (newLabel != null) {
            groupController.editLabel(false, newLabel, label)
        } else {
            groupController.deleteLabel(label)
        }

        labelText.text = label.name
        center = labelText
        right = HBox(colorPicker, deleteButton)
    }

    override fun cancelEdit() {
        labelText.text = label.name
        center = labelText
        right = HBox(colorPicker, deleteButton)
    }

    override fun deleteLabel() {
        val alert = Alert(AlertType.CONFIRMATION)
        alert.title = "Confirmation Dialog"
        alert.headerText = "Are you sure you want to delete this label?"

        val result: Optional<ButtonType> = alert.showAndWait()
        if (result.get() === ButtonType.OK) {
            groupController.deleteLabel(label)
        } else {
            // do nothing
        }
    }
}
