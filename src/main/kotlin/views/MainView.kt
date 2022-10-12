package views

import tornadofx.*;

class MainView: View() {
    override val root = borderpane {
        title = "TODO list" // TODO: if we want to eventually support localization, save this as an i18n resource

        left<Sidepane>()
        center<GroupView>()
    }
}