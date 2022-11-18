package views

import javafx.scene.control.Button
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import javafx.scene.layout.BorderPane

abstract class AddLabelChip() : BorderPane() {
    protected val addLabelButton = Button("+")
    protected val addLabelComboBox = AutocompleteComboBox()

    init {
        /* region styling */
        maxHeight = LabelView.LABEL_HEIGHT
        styleClass.add("item__add-label-chip")
        addLabelButton.styleClass.add("item__add-label-chip__button")
        addLabelComboBox.styleClass.add("item__add-label-chip__combo-box")
        /* end region styling */

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
                KeyCode.ENTER -> {
                    commitEdit()
                    event.consume()
                }
                KeyCode.ESCAPE -> {
                    cancelEdit()
                }
                else -> Unit
            }
        }

        /* end region event filters */
        center = addLabelButton
        addLabelComboBox.isEditable = true
    }

    abstract fun startEdit()
    abstract fun commitEdit()

    protected fun cancelEdit() {
        center = addLabelButton
    }
}
