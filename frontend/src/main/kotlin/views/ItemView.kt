package views

import controllers.GroupViewController
import javafx.beans.value.ChangeListener
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.scene.control.Button
import javafx.scene.control.ComboBox
import javafx.scene.control.ScrollPane
import javafx.scene.control.TextField
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import javafx.scene.input.MouseEvent
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import jfxtras.scene.control.LocalDateTimeTextField
import models.Item
import models.Label
import models.Priority
import javafx.scene.layout.Priority as JfxPriority

class ItemView(private val controller: GroupViewController, private val item: Item) : BorderPane() {
    private val LABEL_VIEW_GUTTER_LENGTH = 12.0

    private val textField = TextField()
    private val completionButton = Button()
    private val deleteButton = Button("x")
    private val propertyContainer = HBox()
    private val priorityPickerContainer = HBox()
    private val priorityPicker = ComboBox<Priority>()
    private val dueDatePicker = LocalDateTimeTextField()
    private val labelViewScrollContainer = ScrollPane()
    private val labelViewContainer = HBox(LABEL_VIEW_GUTTER_LENGTH)

    init {
        /* region styling */
        styleClass.addAll("item")
        textField.styleClass.addAll("body", "item__heading")
        deleteButton.styleClass.addAll("item__delete-button")
        completionButton.styleClass.addAll("item__completion-button")
        labelViewScrollContainer.styleClass.addAll("item__label-container")
        labelViewContainer.styleClass.add("item__label-content")
        priorityPickerContainer.styleClass.add("item__priority-picker-content")
        priorityPicker.styleClass.addAll("item__priority-picker", "list-cell", "label-max")
        priorityPicker.minWidth = 130.0
        /* end region styling */

        /* region item setup */
        setupTextField()
        setupLabelViewContainer()
        setupPriorityPicker()
        setupLocalDateTimePicker()
        configDeleteButton(item)
        configCompletionButton(item)
        /* end region item setup */

        propertyContainer.children.addAll(priorityPickerContainer, dueDatePicker, labelViewScrollContainer)
        HBox.setHgrow(labelViewScrollContainer, JfxPriority.ALWAYS)

        left = completionButton
        right = deleteButton
        center = textField
        bottom = propertyContainer

        /* hovering and dragging setup */
        this.hoverProperty().addListener { _, _, newValue ->
            if (newValue) {
                controller.setOpenHandCursor()
            } else {
                controller.resetCursor()
            }
        }

        this.setOnMouseDragged { event ->
            controller.setClosedHandCursor()
            controller.setItemNewYPosition(item, this.layoutY + event.y)
        }
        /* hovering and dragging setup end */
    }

    private fun focusItem() {
        controller.focusItem(item)
        left = null
        right = null
    }

    private fun unfocusItem() {
        controller.clearFocus()
        left = completionButton
        right = deleteButton
    }

    private fun setupLabelViewContainer() {
        labelViewScrollContainer.isFitToHeight = true
        labelViewScrollContainer.prefHeight = 62.0
        labelViewScrollContainer.vbarPolicy = ScrollPane.ScrollBarPolicy.NEVER // hide vertical scroll bar
        labelViewScrollContainer.vmax = 0.0 // prevent vertical scrolling
        labelViewScrollContainer.content = labelViewContainer

        var labelChips = listOf<BorderPane>()
        val itemLabels: List<Label> = controller.labelListProperty.filter {
                label ->
            label.id in item.labelIds
        }
        if (itemLabels.isNotEmpty()) {
            labelChips = itemLabels.map {
                ItemLabelView(groupController = controller, label = it, item = item)
            }
        }

        labelViewContainer.children.clear()
        if (labelChips.isNotEmpty()) {
            labelViewContainer.children.addAll(labelChips)
        }
        labelViewContainer.children.add(AddItemLabelChip(controller = controller, item = item))

        // When clicking outside a label, remove focus from that label
        labelViewContainer.addEventHandler(
            MouseEvent.MOUSE_CLICKED,
            EventHandler<MouseEvent> { requestFocus() }
        )
    }

    private fun setupTextField() {
        // hide buttons while textField is focused
        textField.focusedProperty().addListener { _, _, newValue ->
            when (newValue) {
                true -> startEdit()
                false -> cancelEdit()
            }
        }

        // when unfocusing a textField, save changes
        textField.onAction = EventHandler { _: ActionEvent? ->
            commitEdit(item)
        }

        // when the ESC key is pressed, undo staged changes
        textField.addEventFilter(
            KeyEvent.KEY_RELEASED
        ) { e: KeyEvent ->
            if (e.code == KeyCode.ESCAPE) {
                cancelEdit()
            }
        }

        textField.text = item.title
    }

    private fun setupPriorityPicker() {
        priorityPickerContainer.children.add(priorityPicker)
        priorityPicker.items.add(null)
        priorityPicker.items.addAll(Priority.values())
        priorityPicker.value = item.priority

        priorityPicker.valueProperty().addListener(
            ChangeListener { _, oldValue, newValue ->
                if (oldValue != newValue) {
                    val originalItem = item.copy()
                    item.priority = priorityPicker.value
                    controller.editItem(item, originalItem)
                }
            }
        )
    }

    private fun setupLocalDateTimePicker() {
        item.edtDueDate?.let { dueDatePicker.localDateTime = it }

        dueDatePicker.localDateTimeProperty().addListener(
            ChangeListener { _, oldValue, newValue ->
                if (oldValue != newValue) {
                    val originalItem = item.copy()
                    item.edtDueDate = dueDatePicker.localDateTime
                    controller.editItem(item, originalItem)
                }
            }
        )
    }

    /* region lifecycle methods */
    private fun startEdit() {
        // hide delete and completion button
        focusItem()

        textField.text = item.title
        textField.requestFocus()
    }

    private fun cancelEdit() {
        // show completion and deletion button
        unfocusItem()
        textField.text = item.title
    }

    private fun commitEdit(item: Item?) {
        if (item != null) {
            val originalItem = item.copy()
            item.title = textField.text
            controller.editItem(item, originalItem)
        }
    }
    /* end region lifecycle methods */

    private fun configDeleteButton(item: Item) {
        deleteButton.setOnAction {
            controller.deleteItem(item)
        }
    }

    private fun configCompletionButton(item: Item) {
        val imageUrl = if (item.isCompleted) "completed_item_48.png" else "uncompleted_item_48.png"
        val image = Image(imageUrl)
        val view = ImageView(image)
        view.fitHeight = 18.0
        view.isPreserveRatio = true
        completionButton.graphic = view
        completionButton.setOnAction {
            val newItem = item.copy()
            newItem.isCompleted = !item.isCompleted
            controller.editItem(newItem, item)
        }
    }
}
