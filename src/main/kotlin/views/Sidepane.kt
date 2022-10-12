package views

import controllers.SidepaneController
import javafx.scene.Parent
import tornadofx.*;

class Sidepane() : View() {
    val sidepaneController: SidepaneController by inject()

    override val root: Parent = vbox {
        listmenu {
            sidepaneController.groups().map { group ->
                button(group.name) {
                    action { sidepaneController.focusGroup(group)}
                }
            }
        }
    }
}