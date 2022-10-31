package views

import controllers.GroupViewController
import javafx.beans.value.ChangeListener
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.scene.control.Button
import javafx.scene.control.ListCell
import javafx.scene.control.TextField
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import javafx.scene.layout.BorderPane
import models.Item

class ItemView(private val controller: GroupViewController): ListCell<Item>() {
        private val root = BorderPane()
        private val textField = TextField()
        private val deleteButton = Button("x")

        init {
            root.left = deleteButton
            root.center = textField

            graphic = root


            /* region textField setup */
            // hide delete button while textField is focused
            textField.focusedProperty().addListener(ChangeListener { _, _, newValue ->
                when (newValue) {
                    true -> focusItem()
                    false -> unfocusItem()
                }
            })

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
            /* end region textField setup */
        }

    fun focusItem() {
       root.left = null
    }

    fun unfocusItem() {
        root.left = deleteButton
    }

    override fun updateItem(item: Item?, empty: Boolean) {
        super.updateItem(item, empty)
        if (empty) {
            graphic = null
            return
        }

        graphic = root
        if (isEditing) {
            textField.text = item?.title

        } else {
            if (item != null) {
                textField.text = item.title
                deleteButton.setOnAction {
                    controller.deleteItem(item)
                    println("deleted")
                }
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
}