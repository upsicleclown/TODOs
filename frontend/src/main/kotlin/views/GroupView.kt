package views

import controllers.GroupViewController
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.ListCell
import javafx.scene.control.ListView
import javafx.scene.control.TextField
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import javafx.scene.layout.HBox
import javafx.scene.layout.Priority
import javafx.scene.layout.Region
import javafx.scene.layout.VBox
import models.Group
import models.Item

class GroupView(var controller: GroupViewController) : VBox() {

    private var currentGroupName = Label()
    private val listView = ListView<Item>()

    init {
        listView.isEditable = true

        // Add name of current group at the top.
        children.add(currentGroupName)

        // Add a text field to create new items.
        children.add(buildItemCreationTextField())

        // Set listView's cell factory.
        // Inspired from https://stackoverflow.com/questions/35963888/how-to-create-a-listview-of-complex-objects-and-allow-editing-a-field-on-the-obj
        listView.setCellFactory { _: ListView<Item?>? ->
            object : ListCell<Item?>() {
                override fun updateItem(item: Item?, empty: Boolean) {
                    super.updateItem(item, empty)
                    if (item == null || empty) {
                        text = null
                        graphic = null
                    } else {
                        // Build layout for each ListCell.
                        val root = HBox(10.0)
                        root.alignment = Pos.CENTER_LEFT
                        root.padding = Insets(5.0, 10.0, 5.0, 10.0)

                        // First, add text field for the item's title.
                        root.children.add(buildItemTextField(item))

                        // Second, add region to push buttons to the right.
                        val region = Region()
                        HBox.setHgrow(region, Priority.ALWAYS)
                        root.children.add(region)

                        // Third, add buttons
                        val completeButton = buildCompleteUnCompleteButton(item)
                        val removeButton = buildDeleteButton(item)
                        root.children.addAll(completeButton, removeButton)

                        // Finally, set our cell to display root.
                        text = null
                        graphic = root
                    }
                }
            }
        }

        // Add listview.
        children.add(listView)
    }

    fun refreshWithItems(group: Group, items: List<Item>) {
        listView.items.clear()
        children[0] = Label(group.name)
        listView.items.addAll(items)
    }

    private fun buildItemCreationTextField(): TextField {
        val itemCreationField = TextField()
        itemCreationField.promptText = "Create a new item..."
        // When enter is pressed.
        itemCreationField.setOnAction {
            controller.createItem(Item(itemCreationField.text, false))
            itemCreationField.text = ""
        }
        return itemCreationField
    }

    private fun buildItemTextField(item: Item): TextField {
        val textField = TextField(item.title)
        textField.setOnAction {
            val newItem = item.copy()
            newItem.title = textField.text
            controller.editItem(newItem, item)
        }
        textField.focusedProperty().addListener { _, _, newPropertyValue ->
            if (!newPropertyValue) {
                val newItem = item.copy()
                newItem.title = textField.text
                controller.editItem(newItem, item)
            }
        }
        textField.addEventFilter(
            KeyEvent.KEY_RELEASED
        ) { e: KeyEvent ->
            if (e.code == KeyCode.ESCAPE) {
                textField.text = item.title
            }
        }
        return textField
    }

    private fun buildCompleteUnCompleteButton(item: Item): Button {
        val button = Button()
        val imageUrl = if (item.isCompleted) "completed_item_48.png" else "uncompleted_item_48.png"
        val image = Image(imageUrl)
        val view = ImageView(image)
        view.fitHeight = 18.0
        view.isPreserveRatio = true
        button.graphic = view
        button.setOnAction {
            val newItem = item.copy()
            newItem.isCompleted = !item.isCompleted
            controller.editItem(newItem, item)
        }
        return button
    }

    private fun buildDeleteButton(item: Item): Button {
        val button = Button("Delete")
        button.setOnAction {
            controller.deleteItem(item)
        }
        return button
    }
}
