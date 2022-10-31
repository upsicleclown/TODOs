package views

import components.ItemContainer
import controllers.GroupViewController
import javafx.collections.FXCollections
import javafx.scene.control.Label
import javafx.scene.control.ListCell
import javafx.scene.control.ListView
import javafx.scene.control.TextField
import javafx.scene.layout.VBox
import javafx.util.Callback
import models.Group
import models.Item

class GroupView(controller: GroupViewController) : VBox() {

    val focusedGroupItemsSource = FXCollections.observableArrayList<Item>()
    var currentGroupName = Label("")
    private val listView = ListView<ItemContainer<Item>>()

    init {
        children.add(currentGroupName)

        // create a textfield
        val itemCreationField = TextField()
        itemCreationField.promptText = "Create a new item..."

        // when enter is pressed
        itemCreationField.setOnAction {
            controller.createItem(Item(itemCreationField.text, false))
            itemCreationField.text = ""
        }

        children.add(itemCreationField)

        listView.isEditable = true

//        listView.items = focusedGroupItemsSource

        listView.setCellFactory(
            Callback<ListView<ItemContainer<Item>>, ListCell<ItemContainer<Item>>> {
                ItemContainer(item)
            }
        )

//        children.add(listView)
    }

    fun refreshWithItems(group: Group, items: List<Item>) {
        listView.items.clear()
        children[0] = Label(group.name)
        focusedGroupItemsSource.setAll(items)
        listView.items.addAll(items)
    }
}
