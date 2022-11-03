package views

import javafx.scene.control.ButtonBar
import javafx.scene.control.ButtonType
import javafx.scene.control.Dialog
import javafx.scene.control.TextField
import models.Filter
import models.Group

class GroupCreationView : Dialog<Group?>() {
    init {
        title = "Create a Group"

        val createButtonType = ButtonType("Create", ButtonBar.ButtonData.YES)
        val cancelButtonType = ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE)
        dialogPane.buttonTypes.addAll(createButtonType, cancelButtonType)

        // field to create groups
        val groupCreationField = TextField()
        groupCreationField.promptText = "Create a new group..."

        setResultConverter { dialogButton ->
            var returnGroup: Group? = null
            if (dialogButton === createButtonType) {
                returnGroup = Group(groupCreationField.text, Filter())
            }
            groupCreationField.text = ""
            return@setResultConverter returnGroup
        }

        dialogPane.content = groupCreationField
    }
}
