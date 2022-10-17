package views

import controllers.SidepaneController
import javafx.collections.FXCollections
import javafx.scene.control.Button
import javafx.scene.control.ListView
import javafx.scene.layout.GridPane
import javafx.scene.layout.VBox
import models.Group
import models.Item

class SidepaneView(sidepaneController: SidepaneController): VBox() {

    var groupButtons = FXCollections.observableArrayList<Button>()

    init {
        groupButtons.setAll(sidepaneController.groups().forEach { group ->
            var button = Button(group.name)
            button.setOnAction { sidepaneController.focusGroup(group) }
        })
        val listView = ListView(groupButtons)
    }

//    override val root: Parent = vbox {
//        listmenu {
//            sidepaneController.groups().map { group ->
//                button(group.name) {
//                    action { sidepaneController.focusGroup(group)}
//                }
//            }
//        }
//    }
}