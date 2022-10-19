package ui.views

import javafx.collections.FXCollections
import javafx.scene.control.Label
import javafx.scene.control.ListView
import javafx.scene.layout.VBox
import models.Group
import models.Item
import ui.controllers.GroupViewController

class GroupView(@Suppress("UNUSED_PARAMETER") controller: GroupViewController) : VBox() {

    val focusedGroupItemsSource = FXCollections.observableArrayList<Item>()
    var currentGroupName = Label("")

    fun refreshWithItems(group: Group, items: List<Item>) {
        children.clear()
        currentGroupName = Label(group.name)
        children.add(currentGroupName)
        focusedGroupItemsSource.setAll(items)
        val listView = ListView<String>()
        for (item in focusedGroupItemsSource) {
            listView.items.add(item.title)
        }
        children.add(listView)
    }
}
