package views

import controllers.GroupViewController
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.geometry.Orientation
import javafx.scene.control.Button
import javafx.scene.control.ComboBox
import javafx.scene.control.ListCell
import javafx.scene.control.ListView
import javafx.scene.control.ScrollPane
import javafx.scene.control.TextField
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import javafx.scene.layout.BorderPane
import javafx.scene.text.Text
import models.Item
import models.Label

class ItemView(private val controller: GroupViewController) : ListCell<Item>() {
    private val root = BorderPane()
    private val textField = TextField()
    private val completionButton = Button()
    private val deleteButton = Button("x")
    private val labelViewContainer = ScrollPane()
    private val labelView = ListView<BorderPane>()

    private val DEFAULT_LABEL_COLOR = "#89CFF0"

    init {
        root.left = completionButton
        root.right = deleteButton
        root.center = textField
        root.bottom = labelViewContainer
        labelViewContainer.isFitToWidth = true
        graphic = root
    }

    private fun labelToLabelChip(label: Label): BorderPane {
        val root = BorderPane()
        val labelText = Text(label.name)
        val deleteButton = Button("x")
        deleteButton.setOnAction {
            val originalItem = item.copy()
            val newItem = item.copy()
            newItem.labelIds.remove(label.id)

            if (newItem != null && originalItem != null) {
                controller.editItem(newItem, originalItem)
            }
        }
        root.top = null
        root.center = labelText
        root.right = deleteButton
        root.left = null
        return root
    }

    fun focusItem() {
        root.left = null
        root.right = null
    }

    fun unfocusItem() {
        root.left = completionButton
        root.right = deleteButton
    }

    private fun setupLabelView() {
        labelViewContainer.isFitToHeight = true
        labelViewContainer.prefHeight = 54.0
        labelViewContainer.vbarPolicy = ScrollPane.ScrollBarPolicy.NEVER
        labelViewContainer.content = labelView
        labelView.orientation = Orientation.HORIZONTAL

        var labelChips = listOf<BorderPane>()
        val itemLabels: List<Label> = controller.labels().filter {
                label ->
            label.id in item.labelIds
        }
        if (itemLabels.isNotEmpty()) {
            labelChips = itemLabels.map {
                labelToLabelChip(it)
            }
        }

        val addLabelChip = BorderPane()
        val addLabelButton = Button("+")
        val addLabelComboBox = ComboBox<String>()
        addLabelComboBox.isEditable = true
        addLabelComboBox.items.addAll(controller.labels().map { label -> label.name })
        addLabelComboBox.addEventFilter(
            KeyEvent.KEY_RELEASED
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

            val originalItem = item?.copy()
            val newItem = item?.copy()
            newItem?.labelIds?.add(newLabel.id)
            if (newItem != null && originalItem != null) {
                controller.editItem(newItem, originalItem)
            }
            addLabelChip.center = addLabelButton
        }

        addLabelChip.center = addLabelButton
        addLabelButton.setOnAction {
            addLabelChip.center = addLabelComboBox
        }

        labelView.items.clear()
        if (labelChips.isNotEmpty()) {
            labelView.items.addAll(labelChips)
        }
        labelView.items.add(addLabelChip)
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

    override fun updateItem(item: Item?, empty: Boolean) {
        super.updateItem(item, empty)
        if (empty) {
            graphic = null
            return
        }

        setupTextField()
        setupLabelView()

        graphic = root
        if (isEditing) {
            textField.text = item?.title
        } else {
            if (item != null) {
                textField.text = item.title

                configDeleteButton(item)
                configCompletionButton(item)
            }
        }
    }

    override fun startEdit() {
        super.startEdit()
        // hide delete button
        root.left = null

        textField.text = item?.title
        textField.selectAll()
        textField.requestFocus()
    }

    override fun cancelEdit() {
        super.cancelEdit()
        textField.text = item?.title
    }

    override fun commitEdit(item: Item?) {
        super.commitEdit(item)
        if (item != null) {
            val originalItem = item.copy()
            item.title = textField.text
            controller.editItem(item, originalItem)
        }
    }

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
