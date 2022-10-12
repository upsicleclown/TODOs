package views

import controllers.SidepaneController
import javafx.scene.Parent
import javafx.scene.layout.VBox
import tornadofx.View

class Sidepane() : View() {
    val controller: SidepaneController by inject()

    override val root: Parent = VBox()

    init {

    }
}