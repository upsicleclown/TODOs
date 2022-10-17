package views

import controllers.GroupViewController
import javafx.collections.FXCollections
import javafx.scene.layout.GridPane
import models.Item
import java.util.*
import javafx.scene.control.ListView
import javafx.scene.layout.VBox


class GroupView(controller: GroupViewController): VBox()  {
    val focusedGroupItemsSource = FXCollections.observableArrayList<Item>()

    init {
        var root = GridPane()
        val listView = ListView(focusedGroupItemsSource)
    }

    fun refreshWithItems(items: List<Item>) {
        focusedGroupItemsSource.setAll(items)
    }
}