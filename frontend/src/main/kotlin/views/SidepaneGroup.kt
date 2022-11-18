package views

import controllers.SidepaneController
import javafx.animation.Interpolator
import javafx.animation.KeyFrame
import javafx.animation.KeyValue
import javafx.animation.Timeline
import javafx.beans.property.SimpleBooleanProperty
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.geometry.Pos
import javafx.scene.control.Button
import javafx.scene.control.TextField
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import javafx.scene.input.MouseEvent
import javafx.scene.layout.BorderPane
import javafx.util.Duration
import models.Group
import javafx.scene.control.Label as JfxLabel

class SidepaneGroup(private val sidepaneController: SidepaneController, private val group: Group) : BorderPane() {
    private val textField = TextField() // For modifying group name, initally hidden
    private val label = JfxLabel(group.name) // For displaying group name
    private val deleteButton = Button("x")
    private var mouseReleased = false // A boolean flag to determine whether to cancel long press
    private var longPressCounter = 0.0 // Timer for determining whether to focus group on mouse release
    private var longPress = SimpleBooleanProperty(false) // An Observable flag that triggers long press functionality
    private val LONG_PRESS_TIME_QUANTUM = 1000.0 // 1.0 Second duration for long press recognition (in ms)

    init {
        /* region styling */
        if (sidepaneController.focusedGroupProperty.value == group) {
            styleClass.setAll("sidepane__group--selected")
        } else {
            styleClass.setAll("sidepane__group")
        }
        // TODO: the font size for labels should be variable
        label.styleClass.addAll("sidepane__group__label", "body")
        deleteButton.styleClass.addAll("sidepane__group__delete-button", "body")
        textField.styleClass.addAll("sidepane__group__text-field", "body")
        /* end region styling */

        /* region event filters */
        // When our group is focused, change style class to reflect that
        sidepaneController.focusedGroupProperty.addListener { _, _, focusedGroup ->
            when (focusedGroup) {
                group -> styleClass.setAll("sidepane__group--selected")
                else -> styleClass.setAll("sidepane__group")
            }
        }

        // Handling mouse click/long press
        longPress.addListener { _, _, triggered ->
            when (triggered) {
                true -> longPress()
                false -> Unit
            }
        }

        addEventHandler(
            MouseEvent.ANY,
            EventHandler { event ->
                when (event.eventType) {
                    MouseEvent.MOUSE_PRESSED -> {
                        sidepaneController.focusGroup(group)

                        mouseReleased = false
                        longPressCounter = System.currentTimeMillis().toDouble()

                        // JavaFX only supports delaying changes on the application thread
                        // This is only possible if we use their animation library instead
                        // of the Kotlin concurrent scheduling library.
                        var scheduler = Timeline()
                        scheduler.cycleCount = 1
                        scheduler.keyFrames.add(
                            KeyFrame(
                                Duration(LONG_PRESS_TIME_QUANTUM),
                                KeyValue(longPress, true, Interpolator.DISCRETE)
                            )
                        )
                        scheduler.play()
                    }
                    MouseEvent.MOUSE_CLICKED -> { // This case prevents SidepaneView from stealing focus
                        mouseReleased = true
                        if (event.clickCount == 2) startEdit()
                        event.consume()
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
        /* end region event filters */

        left = label
        label.alignment = Pos.BASELINE_CENTER
        label.maxHeight = 25.0
        center = null
        right = deleteButton
        deleteButton.maxHeight = 25.0
        deleteButton.alignment = Pos.BASELINE_CENTER
    }

    private fun longPress() {
        if (!mouseReleased) startEdit()
        longPress.set(false)
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
