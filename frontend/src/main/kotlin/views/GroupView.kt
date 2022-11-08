package views

import controllers.GroupViewController
import javafx.scene.control.Label
import javafx.scene.control.ListView
import javafx.scene.control.TextField
import javafx.scene.layout.Priority
import javafx.scene.layout.VBox
import models.Group
import models.Item

class GroupView(controller: GroupViewController) : VBox() {

    private var currentGroupName = Label("")
    private val listView = ListView<Item>()

    init {
        setVgrow(listView, Priority.ALWAYS)
        children.add(currentGroupName)

        // create a text-field
        val itemCreationField = TextField()
        itemCreationField.promptText = "Create a new item..."

        // when enter is pressed
        itemCreationField.setOnAction {
            controller.createItem(Item(itemCreationField.text, false))
            itemCreationField.text = ""
        }

        children.add(itemCreationField)

        listView.isEditable = true

        // from https://stackoverflow.com/questions/35963888/how-to-create-a-listview-of-complex-objects-and-allow-editing-a-field-on-the-obj
        listView.setCellFactory { _: ListView<Item?>? -> ItemView(controller) }

        children.add(listView)
    }

    fun refreshWithItems(group: Group?, items: List<Item>) {
        listView.items.clear()
        children[0] = Label(group?.name)
        listView.items.addAll(items)
    }

    /**
     * Returns the focused item in the list view if any.
     */
    fun getFocusedItem(): Item? {
        return listView.selectionModel.selectedItem
    }
}
