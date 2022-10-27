package views

import controllers.GroupViewController
import javafx.collections.FXCollections
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.ListCell
import javafx.scene.control.ListView
import javafx.scene.control.TextField
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import javafx.scene.layout.VBox
import models.Group
import models.Item

class GroupView(controller: GroupViewController) : VBox() {

    val focusedGroupItemsSource = FXCollections.observableArrayList<Item>()
    var currentGroupName = Label("")
    private val listView = ListView<Item>()

    init {
        children.add(currentGroupName)

        // create a textfield
        val itemCreationField = TextField()
        itemCreationField.promptText = "Create a new item..."

        // when enter is pressed
        itemCreationField.setOnAction {
            controller.createItem(itemCreationField.text)
            itemCreationField.text = ""
        }

        children.add(itemCreationField)

        listView.isEditable = true

        // from https://stackoverflow.com/questions/35963888/how-to-create-a-listview-of-complex-objects-and-allow-editing-a-field-on-the-obj
        listView.setCellFactory { _: ListView<Item?>? ->
            object : ListCell<Item?>() {
                private val textField = TextField()

                init {
                    textField.onAction = EventHandler { _: ActionEvent? ->
                        commitEdit(
                            item
                        )
                    }

                    textField.addEventFilter(
                        KeyEvent.KEY_RELEASED
                    ) { e: KeyEvent ->
                        if (e.code == KeyCode.ESCAPE) {
                            cancelEdit()
                        }
                    }
                }

                private val deleteButton = Button("x")

                override fun updateItem(item: Item?, empty: Boolean) {
                    super.updateItem(item, empty)
                    if (empty) {
                        text = null
                        graphic = null
                    } else if (isEditing) {
                        textField.text = item?.title
                        text = null
                        graphic = textField
                    } else {
                        if (item != null) {
                            text = item.title
                            graphic = deleteButton
                            deleteButton.setOnAction {
                                controller.deleteItem(item)
                            }
                        }
                    }
                }

                override fun startEdit() {
                    super.startEdit()
                    textField.text = item?.title
                    text = null
                    graphic = textField
                    textField.selectAll()
                    textField.requestFocus()
                }

                override fun cancelEdit() {
                    super.cancelEdit()
                    text = item?.title
                    graphic = null
                }

                override fun commitEdit(item: Item?) {
                    super.commitEdit(item)
                    if (item != null) {
                        item.title = textField.text
                        text = textField.text
                        graphic = null
                        controller.editItem(item)
                    }
                }
            }
        }

        children.add(listView)
    }

    fun refreshWithItems(group: Group, items: List<Item>) {
        listView.items.clear()
        children[0] = Label(group.name)
        focusedGroupItemsSource.setAll(items)
        listView.items.addAll(items)
    }
}
