package views

import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.scene.control.Button
import javafx.scene.control.ButtonBar
import javafx.scene.control.ButtonType
import javafx.scene.control.Dialog
import javafx.scene.control.TextField
import models.Filter
import models.Group

class GroupCreationView : Dialog<Group?>() {
    init {
        title = "Create a Group"

        // field to create groups
        val groupCreationField = TextField()
        groupCreationField.promptText = "Create a new group..."
        dialogPane.content = groupCreationField

        // buttons
        val createButtonType = ButtonType("Create", ButtonBar.ButtonData.YES)
        dialogPane.buttonTypes.addAll(createButtonType, ButtonType.CANCEL)

        val createButton = dialogPane.lookupButton(createButtonType) as Button
        createButton.addEventFilter(
            ActionEvent.ACTION
        ) { event ->
            // Check whether the textfield is empty
            if (groupCreationField.text == "") {
                // The conditions are not fulfilled so that we consume the event to prevent the dialog from closing
                event.consume()
            }
        }

        // ensure initial state is clear
        onShowing = EventHandler {
            result = null
            groupCreationField.text = ""
        }

        setResultConverter { dialogButton ->
            if (dialogButton === createButtonType) {
                result = Group(groupCreationField.text, Filter())
            }
            return@setResultConverter result
        }
    }
}
