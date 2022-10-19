package ui.views

import javafx.collections.FXCollections
import javafx.scene.control.Button
import javafx.scene.control.ListView
import javafx.scene.layout.VBox
import ui.controllers.SidepaneController

class SidepaneView(sidepaneController: SidepaneController) : VBox() {

    var groupButtons = FXCollections.observableArrayList<Button>()

    init {
        sidepaneController.groups().forEach { group ->
            var button = Button(group.name)
            button.setOnAction { sidepaneController.focusGroup(group) }
            groupButtons.add(button)
        }
        val listView = ListView(groupButtons)
        this.children.add(listView)
    }
}
