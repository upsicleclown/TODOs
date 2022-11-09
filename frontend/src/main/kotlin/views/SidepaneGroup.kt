package views

import controllers.SidepaneController
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.geometry.Pos
import javafx.scene.control.Button
import javafx.scene.control.TextField
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import javafx.scene.input.MouseEvent
import javafx.scene.layout.BorderPane
import models.Group
import javafx.scene.control.Label as JfxLabel

class SidepaneGroup(private val sidepaneController: SidepaneController, private val group: Group) : BorderPane() {
    private val textField = TextField() // For modifying group name, initally hidden
    private val label = JfxLabel(group.name) // For displaying group name
    private val deleteButton = Button("x")
    private var longPressCounter = 0L // Timer for determining long press duration
    private val LONG_PRESS_TIME_QUANTUM = 1500L // 1.5 Second duration for long press recognition (in ms)

    init {
        /* region styling */
        styleClass.addAll("sidepane__group")
        // TODO: the font size for labels should be variable
        label.styleClass.addAll("sidepane__group__label", "body")
        deleteButton.styleClass.addAll("sidepane__group__delete-button", "body")
        /* end region */

        /* region event filters */
        // When our group is focused, change style class to reflect that
        sidepaneController.focusedGroup().addListener { _, _, focusedGroup ->
            when (focusedGroup) {
                group -> styleClass.setAll("sidepane__group--selected")
                else -> styleClass.setAll("sidepane__group")
            }
        }

        // Handling mouse click/long press
        addEventHandler(
            MouseEvent.ANY,
            EventHandler { event ->
                when (event.eventType) {
                    MouseEvent.MOUSE_PRESSED -> longPressCounter = System.currentTimeMillis()
                    MouseEvent.MOUSE_RELEASED ->
                        if (System.currentTimeMillis() - longPressCounter >= LONG_PRESS_TIME_QUANTUM) {
                            // When group is long pressed, enter group-name modification flow
                            startEdit()
                        } else {
                            // When group is clicked on, switch to relevant GroupView
                            sidepaneController.focusGroup(group)
                        }
                }
            }
        )

        // When delete button is pressed, delete group
        deleteButton.setOnAction {
            sidepaneController.deleteGroup(group)
        }

        // When enter is pressed, commit group edit
        textField.onAction = EventHandler { _: ActionEvent? ->
            commitEdit(group)
        }

        // Use the escape key to cancel edit
        textField.addEventFilter(
            KeyEvent.KEY_RELEASED
        ) { e: KeyEvent ->
            if (e.code == KeyCode.ESCAPE) {
                cancelEdit()
            }
        }

        // Cancel edit if focus is lost
        textField.focusedProperty().addListener { _, _, newValue ->
            when (newValue) {
                true -> Unit // do nothing
                false -> cancelEdit()
            }
        }
        /* end region */

        left = label
        label.alignment = Pos.BASELINE_CENTER
        label.maxHeight = 25.0
        center = null
        right = deleteButton
        deleteButton.maxHeight = 25.0
        deleteButton.alignment = Pos.BASELINE_CENTER
    }

    private fun startEdit() {
        textField.text = group.name

        left = null
        center = textField
        right = null

        textField.selectAll()
        textField.requestFocus()
    }

    private fun cancelEdit() {
        label.text = group.name

        left = label
        center = null
        right = deleteButton
    }

    private fun commitEdit(group: Group?) {
        if (group != null) {
            val originalGroup = group.copy()
            group.name = textField.text
            sidepaneController.editGroup(group, originalGroup)

            label.text = group.name
            left = label
            center = null
            right = deleteButton
        }
    }
}
