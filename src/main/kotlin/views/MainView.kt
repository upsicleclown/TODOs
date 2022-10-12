package views

import controllers.GroupViewController
import controllers.SidepaneController
import tornadofx.*;

class MainView: View() {
    val sidepaneController : SidepaneController by inject()
    val groupViewController : GroupViewController by inject()

    override val root = borderpane {
        title = "TODO list" // TODO: if we want to eventually support localization, save this as an i18n resource

        left {
            listmenu {
                sidepaneController.groups().map { group ->
                    button(group.name) {
                        action { sidepaneController.focusGroup(group)}
                    }
                }
            }
        }

        center<GroupView>()
    }
}