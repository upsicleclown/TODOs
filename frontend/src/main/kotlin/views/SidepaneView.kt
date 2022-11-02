package views

import controllers.SidepaneController
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.scene.control.Button
import javafx.scene.control.ListCell
import javafx.scene.control.ListView
import javafx.scene.control.TextField
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import javafx.scene.layout.Priority
import javafx.scene.layout.VBox
import models.Group

class SidepaneView(sidepaneController: SidepaneController) : VBox() {

    private var listView = ListView<Group>()
    val controller = sidepaneController

    init {
        setVgrow(listView, Priority.ALWAYS)
        listView.isEditable = true

        listView.setCellFactory { _: ListView<Group?>? ->
            object : ListCell<Group?>() {
                private val textField = TextField()

                init {
                    this.onMousePressed = EventHandler { sidepaneController.focusGroup(item) }
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

                override fun updateItem(group: Group?, empty: Boolean) {
                    super.updateItem(group, empty)
                    if (empty) {
                        text = null
                        graphic = null
                    } else if (isEditing) {
                        textField.text = group?.name
                        text = null
                        graphic = textField
                    } else {
                        if (group != null) {
                            text = group.name
                            graphic = deleteButton
                            deleteButton.setOnAction {
                                controller.deleteGroup(group)
                            }
                        }
                    }
                }

                override fun startEdit() {
                    super.startEdit()
                    textField.text = item?.name
                    text = null
                    graphic = textField
                    textField.selectAll()
                    textField.requestFocus()
                }

                override fun cancelEdit() {
                    super.cancelEdit()
                    text = item?.name
                    graphic = deleteButton
                }

                override fun commitEdit(group: Group?) {
                    super.commitEdit(group)
                    if (group != null) {
                        val originalGroup = group.copy()
                        group.name = textField.text
                        text = textField.text
                        graphic = null
                        controller.editGroup(group, originalGroup)
                    }
                }
            }
        }

        listView.items.addAll(controller.groups())
        children.add(listView)

        // field to create groups
        val groupCreationField = TextField()
        groupCreationField.promptText = "Create a new group..."

        groupCreationField.setOnAction {
            controller.createGroup(Group(groupCreationField.text))
            groupCreationField.text = ""
        }

        children.add(groupCreationField)
    }

    fun refreshGroups() {
        listView.items.clear()
        controller.loadGroups()
        listView.items.addAll(controller.groups())
    }
}
