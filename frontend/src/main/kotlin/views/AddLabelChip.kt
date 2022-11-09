package views

import controllers.GroupViewController
import javafx.scene.control.Button
import javafx.scene.control.ComboBox
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import javafx.scene.layout.BorderPane
import models.Item
import models.Label

class AddLabelChip(private val controller: GroupViewController, private val item: Item) : BorderPane() {
    private val addLabelButton = Button("+")
    private val addLabelComboBox = ComboBox<String>()

    init {
        /* region styling */
        maxHeight = LabelView.LABEL_HEIGHT
        styleClass.add("item__add-label-chip")
        addLabelButton.styleClass.add("item__add-label-chip__button")
        addLabelComboBox.styleClass.add("item__add-label-chip__combo-box")
        /* end region */

        /* region event filters */
        // When the combo box loses focus, cancel the edit
        addLabelComboBox.focusedProperty().addListener { _, _, focused ->
            when (focused) {
                true -> Unit
                false -> cancelEdit()
            }
        }

        // Start editing when we click the add button
        addLabelButton.setOnAction { startEdit() }

        addEventFilter(KeyEvent.KEY_PRESSED) { event ->
            when (event.code) {
                KeyCode.ENTER -> commitEdit()
                KeyCode.ESCAPE -> cancelEdit()
                else -> Unit
            }
        }

        /* end region */
        center = addLabelButton
        addLabelComboBox.isEditable = true
        addLabelComboBox.items.addAll(controller.labels().map { label -> label.name })
    }

    private fun startEdit() {
        center = addLabelComboBox
        addLabelComboBox.requestFocus()
    }

    private fun commitEdit() {
        val newLabelName = addLabelComboBox.editor.text.trim()
        if (newLabelName.isBlank()) {
            cancelEdit()
            return
        }
        val existingLabel = controller.labels().any { label -> label.name == newLabelName }

        // if label didn't already exist, then create it
        if (!existingLabel) controller.createLabel(Label(newLabelName, LabelView.DEFAULT_LABEL_COLOR))

        // add new label to item
        val refreshedLabels = controller.labels()
        val newLabel = refreshedLabels.first { label -> label.name == newLabelName }

        val originalItem = item.copy()
        val newItem = item.copy()
        newItem.labelIds.add(newLabel.id)
        controller.editItem(newItem, originalItem)

        center = addLabelButton
    }

    private fun cancelEdit() {
        center = addLabelButton
    }
}
