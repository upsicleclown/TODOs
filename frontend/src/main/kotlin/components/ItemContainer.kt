package components

import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.scene.control.Button
import javafx.scene.control.ListCell
import javafx.scene.control.TextField
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent

class ItemContainer<Item>: ListCell<models.Item>(item: Item) {

    // from https://stackoverflow.com/questions/35963888/how-to-create-a-listview-of-complex-objects-and-allow-editing-a-field-on-the-obj
//    listView.setCellFactory { _: ListView<Item?>? ->
//        object : ListCell<models.Item?>() {
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

    override fun updateItem(item: models.Item, empty: Boolean) {
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

    override fun commitEdit(item: models.Item?) {
        super.commitEdit(item)
        if (item != null) {
            val originalItem = item.copy()
            item.title = textField.text
            text = textField.text
            graphic = null
            controller.editItem(item, originalItem!!)
        }
    }
}
