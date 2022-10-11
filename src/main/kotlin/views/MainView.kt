package views

import tornadofx.View
import tornadofx.borderpane;
import tornadofx.vbox;

class MainView: View() {
    override val root = borderpane () {
        top = vbox()
        title = "TODO list"
        minWidth = 500.0
        maxWidth = 500.0
    }
}