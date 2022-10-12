package views

import controllers.GroupViewController
import controllers.SidepaneController
import javafx.collections.FXCollections
import javafx.collections.ObservableArray
import javafx.collections.ObservableList
import javafx.scene.Parent
import models.Group
import models.InvalidateGroupViewEvent
import models.Item
import tornadofx.*;

class GroupView() : View() {
    val groupViewController: GroupViewController by inject()

    val focusedGroupItemsSource = ArrayList<Item>() /* DO NOT directly access */
    val focusedGroupItems = focusedGroupItemsSource.asObservable()


    override val root = borderpane {
        /* insert things that show up in every group view here */

        /* placeholder listview representing the focused group items */
        left {
            listview(focusedGroupItems)
        }
    }

    init {
        subscribe<InvalidateGroupViewEvent> { event ->
            focusedGroupItems.setAll(event.items)
        }
    }
}