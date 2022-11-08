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
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import models.Item
import models.Label
import models.Priority

class ItemView(private val controller: GroupViewController, private val item: Item) : BorderPane() {
    private val LABEL_VIEW_GUTTER_LENGTH = 12.0
    private val DEFAULT_LABEL_COLOR = "#89CFF0"

    private val textField = TextField()
    private val completionButton = Button()
    private val deleteButton = Button("x")
    private val priorityPicker = ComboBox<Priority>()
    private val labelViewScrollContainer = ScrollPane()
    private val labelViewContainer = HBox(LABEL_VIEW_GUTTER_LENGTH)

    init {
        /* region styling */
        styleClass.addAll("item")
        textField.styleClass.addAll("body", "item__heading")
        deleteButton.styleClass.addAll("item__delete-button")
        completionButton.styleClass.addAll("item__completion-button")
        labelViewScrollContainer.styleClass.addAll("item__label-container")
        labelViewScrollContainer.isFitToWidth = true
        labelViewContainer.styleClass.add("item__label-content")
        /* end region */

        /* region item setup */
        setupTextField()
        setupLabelViewContainer()
        setupPriorityPicker()
        textField.text = item.title
        configDeleteButton(item)
        configCompletionButton(item)
        /* end region */

        left = completionButton
        right = deleteButton
        center = textField
        bottom = labelViewScrollContainer
        labelViewScrollContainer.isFitToWidth = true
    }
    private fun initPicker() {
        priorityPicker.items.add(null)
        priorityPicker.items.addAll(Priority.values())
        priorityPicker.itemsProperty()
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
        val itemLabels: List<Label> = controller.labels().filter {
                label ->
            label.id in item.labelIds
        }
        if (itemLabels.isNotEmpty()) {
            labelChips = itemLabels.map {
                LabelView(groupController = controller, label = it, item = item)
            }
        }

        /* region add label chip */
        val addLabelChip = BorderPane()
        addLabelChip.maxHeight = LabelView.LABEL_HEIGHT
        val addLabelButton = Button("+")
        val addLabelComboBox = ComboBox<String>()

        /* region add label chip styling */
        addLabelChip.styleClass.add("item__add-label-chip")
        addLabelButton.styleClass.add("item__add-label-chip__button")
        addLabelComboBox.styleClass.add("item__add-label-chip__combo-box")
        /* end region */

        addLabelComboBox.isEditable = true
        addLabelComboBox.items.addAll(controller.labels().map { label -> label.name })
        addLabelChip.addEventFilter(
            KeyEvent.KEY_PRESSED
        ) {
                e: KeyEvent ->
            if (e.code != KeyCode.ENTER) return@addEventFilter
            val newLabelName = addLabelComboBox.editor.text.trim()
            if (newLabelName.isBlank()) return@addEventFilter
            val existingLabel = controller.labels().any { label -> label.name == newLabelName }

            // if label didn't already exist, then create it
            if (!existingLabel) controller.createLabel(Label(newLabelName, DEFAULT_LABEL_COLOR))

            // add new label to item
            val refreshedLabels = controller.labels()
            val newLabel = refreshedLabels.first { label -> label.name == newLabelName }

            val originalItem = item.copy()
            val newItem = item.copy()
            newItem.labelIds.add(newLabel.id)
            controller.editItem(newItem, originalItem)

            addLabelChip.center = addLabelButton
        }

        addLabelChip.center = addLabelButton
        addLabelButton.setOnAction {
            addLabelChip.center = addLabelComboBox
        }

        labelViewContainer.children.clear()
        if (labelChips.isNotEmpty()) {
            labelViewContainer.children.addAll(labelChips)
        }
        labelViewContainer.children.add(addLabelChip)
        /* end region */
    }

    private fun setupTextField() {
        // hide buttons while textField is focused
        textField.focusedProperty().addListener { _, _, newValue ->
            when (newValue) {
                true -> focusItem()
                false -> unfocusItem()
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
    }

    private fun setupPriorityPicker() {
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

    /* region lifecycle methods */
    private fun startEdit() {
        // hide delete button
        left = null

        textField.text = item.title
        textField.selectAll()
        textField.requestFocus()
    }

    private fun cancelEdit() {
        textField.text = item.title
    }

    private fun commitEdit(item: Item?) {
        if (item != null) {
            val originalItem = item.copy()
            item.title = textField.text
            controller.editItem(item, originalItem)
        }
    }
    /* end region */

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
