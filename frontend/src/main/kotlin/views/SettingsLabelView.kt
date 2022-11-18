package views

import controllers.GroupViewController
import javafx.scene.control.ColorPicker
import javafx.scene.layout.BorderPane
import javafx.scene.paint.Color
import models.Label

class SettingsLabelView(
    private val groupController: GroupViewController,
    private val label: Label
) : BorderPane() {
    private val labelText = javafx.scene.control.Label(label.name)
    private val colorPicker = ColorPicker(Color.web(label.color))

    init {
        labelText.styleClass.addAll("settings__label-text", "body")
        colorPicker.styleClass.addAll("label-max")

        colorPicker.setOnAction {
            val originalLabel = label.copy()
            val colorHexValue = "#" + colorPicker.value.toString().substring(2, 8)
            label.color = colorHexValue
            groupController.editLabel(false, newLabel = label, originalLabel = originalLabel)
        }

        top = null
        center = labelText
        right = colorPicker
        left = null
    }
}
