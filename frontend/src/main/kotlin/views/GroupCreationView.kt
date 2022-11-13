package views

import controllers.SidepaneController
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.scene.control.Button
import javafx.scene.control.ButtonBar
import javafx.scene.control.ButtonType
import javafx.scene.control.ComboBox
import javafx.scene.control.Dialog
import javafx.scene.control.ScrollPane
import javafx.scene.control.TextField
import javafx.scene.input.MouseEvent
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import models.Filter
import models.Group
import models.Label
import models.Priority

class GroupCreationView(private val controller: SidepaneController) : Dialog<Group?>() {
    var group: Group? = null
    private val priorityPicker = ComboBox<Priority>()
    private val labelViewScrollContainer = ScrollPane()
    private val labelViewContainer = HBox()
    private var groupLabels = listOf<Label>()

    init {
        group = Group("", Filter())
        controller.loadLabels()
        title = "Create a Group"

        // field to create groups
        val groupCreationField = VBox()

        val groupNameField = TextField()
        groupNameField.promptText = "Create a new group..."
        groupCreationField.children.add(groupNameField)

        setupLabelViewContainer()

        val groupFilterField = VBox()

        groupFilterField.children.add(labelViewScrollContainer)

        groupCreationField.children.add(groupFilterField)

        dialogPane.content = groupCreationField

        // buttons
        val createButtonType = ButtonType("Create", ButtonBar.ButtonData.YES)
        dialogPane.buttonTypes.addAll(createButtonType, ButtonType.CANCEL)

        val createButton = dialogPane.lookupButton(createButtonType) as Button
        createButton.addEventFilter(
            ActionEvent.ACTION
        ) { event ->
            // Check whether the textfield is empty
            if (groupNameField.text == "") {
                // The conditions are not fulfilled so that we consume the event to prevent the dialog from closing
                event.consume()
            }
        }

        // ensure initial state is clear
        onShowing = EventHandler {
            result = null
            groupNameField.text = ""
        }

        setResultConverter { dialogButton ->
            if (dialogButton === createButtonType) {
                group?.name = groupNameField.text
                result = group
            }
            return@setResultConverter result
        }
    }

    private fun setupLabelViewContainer() {
        labelViewScrollContainer.isFitToHeight = true
        labelViewScrollContainer.prefHeight = 62.0
        labelViewScrollContainer.vbarPolicy = ScrollPane.ScrollBarPolicy.NEVER // hide vertical scroll bar
        labelViewScrollContainer.vmax = 0.0 // prevent vertical scrolling
        labelViewScrollContainer.content = labelViewContainer

        loadLabelChips()

        // When clicking outside a label, remove focus from that label
        labelViewContainer.addEventHandler(
            MouseEvent.MOUSE_CLICKED,
            EventHandler<MouseEvent> { this.dialogPane.requestFocus() }
        )
    }

    private fun loadLabelChips() {
        var labelChips = listOf<BorderPane>()
        groupLabels = controller.labels().filter {
                label ->
            label.id in group!!.filter!!.labelIds
        }

        if (groupLabels.isNotEmpty()) {
            labelChips = groupLabels.map {
                GroupCreationLabelView(sidepaneController = controller, label = it, group = group!!)
            }
        }

        labelViewContainer.children.clear()
        if (labelChips.isNotEmpty()) {
            labelViewContainer.children.addAll(labelChips)
        }
        labelViewContainer.children.add(AddGroupCreationLabelChip(controller = controller, group = group!!))
    }

    fun refreshLabels() {
        loadLabelChips()
    }
}
