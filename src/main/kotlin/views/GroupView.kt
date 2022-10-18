package views

import controllers.GroupViewController
import javafx.collections.FXCollections
import javafx.scene.control.ListView
import javafx.scene.layout.GridPane
import javafx.scene.layout.VBox
import models.Item

class GroupView(@Suppress("UNUSED_PARAMETER") controller: GroupViewController) : VBox() {

    val focusedGroupItemsSource = FXCollections.observableArrayList<Item>()

    init {
        @Suppress("UNUSED_VARIABLE")
        var root = GridPane()
        val listView = ListView(focusedGroupItemsSource)
        this.children.add(listView)
    }

    fun refreshWithItems(items: List<Item>) {
        focusedGroupItemsSource.setAll(items)
    }
}
