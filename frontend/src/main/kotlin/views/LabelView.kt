package views

import javafx.animation.Interpolator
import javafx.animation.KeyFrame
import javafx.animation.KeyValue
import javafx.animation.Timeline
import javafx.beans.property.SimpleBooleanProperty
import javafx.event.EventHandler
import javafx.scene.control.Button
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import javafx.scene.input.MouseEvent
import javafx.scene.layout.BorderPane
import javafx.scene.layout.Region
import javafx.util.Duration
import models.Label
import javafx.scene.control.Label as JfxLabel

abstract class LabelView(private val label: Label) : BorderPane() {
    protected val labelText = JfxLabel(label.name)
    protected val comboBox = AutocompleteComboBox()
    protected val deleteButton = Button("x")
    private var mouseReleased = false // A boolean flag to determine whether to cancel long press
    private var longPressCounter = 0.0 // Timer for determining whether to focus group on mouse release
    private var longPress = SimpleBooleanProperty(false) // An Observable flag that triggers long press functionality
    private val LONG_PRESS_TIME_QUANTUM = 1000.0 // 1.0 Second duration for long press recognition (in ms)

    init {
        /* region styling */
        style = "-fx-background-color:${label.color};"
        styleClass.add("item__label-chip")
        maxHeight = LABEL_HEIGHT

        labelText.styleClass.addAll("label-max", "item__label-chip__text")
        labelText.minWidth = Region.USE_PREF_SIZE // do not crush text as we start to overflow the scroll pane

        deleteButton.styleClass.addAll("label-max", "item__label-chip__delete-button")
        /* end region styling */

        /* region event filters */
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
                    MouseEvent.MOUSE_CLICKED -> { // This case prevents LabelViewContainer from stealing focus
                        mouseReleased = true
                        if (event.clickCount == 2) startEdit()
                        event.consume()
                    }
                }
            }
        )

        // When the Text Field loses focus, cancel the edit
        comboBox.focusedProperty().addListener { _, _, newValue ->
            when (newValue) {
                true -> Unit
                false -> cancelEdit()
            }
        }

        // When we press enter/esc, commit/cancel the edit
        comboBox.addEventFilter(KeyEvent.KEY_PRESSED) { event ->
            when (event.code) {
                KeyCode.ENTER -> {
                    commitEdit()
                    event.consume()
                }
                KeyCode.ESCAPE -> cancelEdit()
                else -> Unit
            }
        }

        deleteButton.setOnAction {
            deleteLabel()
        }
        /* end region event filters */

        comboBox.isEditable = true
        top = null
        center = labelText
        right = deleteButton
        left = null
    }

    private fun longPress() {
        if (!mouseReleased) startEdit()
        longPress.set(false)
    }

    abstract fun startEdit()

    abstract fun commitEdit()

    abstract fun cancelEdit()

    abstract fun deleteLabel()

    companion object {
        const val LABEL_HEIGHT = 32.0
        const val DEFAULT_LABEL_COLOR = "#89CFF0"
    }
}
