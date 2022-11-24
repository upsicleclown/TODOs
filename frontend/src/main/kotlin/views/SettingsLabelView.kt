package views

import controllers.GroupViewController
import javafx.scene.control.ColorPicker
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.paint.Color
import models.Label

class SettingsLabelView(
    private val groupController: GroupViewController,
    private val label: Label
) : LabelView(label) {
//    private val labelText = javafx.scene.control.Label(label.name)
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
        println("newLabelName $newLabelName")

        if (newLabelName.isBlank()) {
            cancelEdit()
            return
        }
        println("spot1")

        var newLabel = if (groupController.labelListProperty.value.any { label -> label.name == newLabelName }) {
            null
        } else {
            if (groupController.labelListProperty.any { l -> l.name == newLabelName }) {
                groupController.labelListProperty.first { l -> l.name == newLabelName }
            } else {
                Label(newLabelName, DEFAULT_LABEL_COLOR)
            }
        }

        print("newLabel, $newLabel")
        print("label, $label")
        if (newLabel != null) {
            println("spot5")
            groupController.editLabel(false, newLabel, label)
        } else {
            println("spot6")
            groupController.deleteLabel(label)
        }
        println("spot7")

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
        groupController.deleteLabel(label)
    }
}
