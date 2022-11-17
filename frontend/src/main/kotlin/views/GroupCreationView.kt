package views

import controllers.SidepaneController
import javafx.beans.value.ChangeListener
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.scene.control.Button
import javafx.scene.control.ButtonBar
import javafx.scene.control.ButtonType
import javafx.scene.control.Dialog
import javafx.scene.control.ScrollPane
import javafx.scene.control.TextField
import javafx.scene.input.MouseEvent
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import models.Filter
import models.Group
import models.Label

class GroupCreationView(private val controller: SidepaneController) : Dialog<Group?>() {
    var group: Group? = null
    private val labelViewScrollContainer = ScrollPane()
    private val labelViewContainer = HBox()
    private val groupCreationField = VBox()
    private val groupNameField = TextField()
    private val groupFilterField = VBox()
    private val createButtonType = ButtonType("Create", ButtonBar.ButtonData.YES)

    init {
        controller.todoApp()?.enableHotkeys(dialogPane.scene)
        dialogPane.scene.stylesheets.add("/style/TODOApplication.css")
        this.isResizable = true
        dialogPane.setMinSize(500.0, 400.0)

        group = Group("", Filter())
        title = "Create a Group"

        // field to add group's name
        groupNameField.promptText = "Create a new group..."
        groupCreationField.children.add(groupNameField)

        // field to add group's filter labels
        setupLabelViewContainer()
        loadLabelViewContainer(listOf())
        groupFilterField.children.add(labelViewScrollContainer)

        groupCreationField.children.add(groupFilterField)
        dialogPane.content = groupCreationField

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

        setUpGroupResultConverter()
    }

    private fun setupLabelViewContainer() {
        labelViewScrollContainer.isFitToHeight = true
        labelViewScrollContainer.prefHeight = 62.0
        labelViewScrollContainer.vbarPolicy = ScrollPane.ScrollBarPolicy.NEVER // hide vertical scroll bar
        labelViewScrollContainer.vmax = 0.0 // prevent vertical scrolling
        labelViewScrollContainer.content = labelViewContainer

        controller.groupCreationLabelListProperty.addListener(
            ChangeListener { _, _, newList ->
                loadLabelViewContainer(newList)
            }
        )

        // When clicking outside a label, remove focus from that label
        labelViewContainer.addEventHandler(
            MouseEvent.MOUSE_CLICKED,
            EventHandler<MouseEvent> { this.dialogPane.requestFocus() }
        )
    }

    private fun loadLabelViewContainer(list: List<Label>) {
        labelViewContainer.children.setAll(
            list.map {
                GroupCreationLabelView(sidepaneController = controller, label = it)
            }
        )
        labelViewContainer.children.add(AddGroupCreationLabelChip(controller = controller, group = group!!))
    }

    private fun setUpGroupResultConverter() {
        setResultConverter { dialogButton ->

            when (dialogButton) {
                (createButtonType) -> {
                    group?.name = groupNameField.text
                    controller.groupCreationLabelListProperty.value.forEach { label ->
                        if (label.name !in controller.labelListProperty.map { it.name }) {
                            controller.createLabel(false, label)
                        }
                    }
                    group?.filter!!.labelIds = controller.labelListProperty.filter { it.name in controller.groupCreationLabelListProperty.value.map { groupCreationLabel -> groupCreationLabel.name } }.map { it.id }.toMutableList()
                    result = group
                }
                else -> result = null
            }

            controller.groupCreationLabelListProperty.setAll(mutableListOf())
            return@setResultConverter result
        }
    }
}
