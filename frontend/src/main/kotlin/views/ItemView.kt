package views

import controllers.GroupViewController
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.scene.control.Button
import javafx.scene.control.ListCell
import javafx.scene.control.TextField
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import models.Item

class ItemView(private val controller: GroupViewController): ListCell<Item>() {
        private val textField = TextField()
        private val deleteButton = Button("x")

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
            val originalItem = item.copy()
            item.title = textField.text
            text = textField.text
            graphic = null
            controller.editItem(item, originalItem)
        }
    }
}