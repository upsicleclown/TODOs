package views

import controllers.SidepaneController
import javafx.beans.value.ChangeListener
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.geometry.Pos
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
import javafx.scene.control.Label as JfxLabel

class GroupCreationView(private val controller: SidepaneController) : Dialog<Group?>() {
    var group = Group("", Filter())
    private var createButton = Button()
    private var cancelButton = Button()
    private val labelViewScrollContainer = ScrollPane()
    private val labelViewContainer = HBox(20.0)
    private val groupCreationContainer = VBox(24.0)
    private val groupNameField = TextField("")
    private val groupFilterLabel = JfxLabel("FILTERS")
    private val groupFilterContainer = VBox()
    private val createButtonType = ButtonType("save", ButtonBar.ButtonData.YES)

    init {
        /* region styling */
        dialogPane.scene.stylesheets.add("/style/TODOApplication.css")
        controller.todoApp()?.enableHotkeys(dialogPane.scene)

        groupCreationContainer.styleClass.add("group-creation")
        groupCreationContainer.alignment = Pos.CENTER
        groupNameField.styleClass.addAll("title-min", "group-creation__text-field")
        groupNameField.maxWidth = 540.0

        groupFilterLabel.styleClass.addAll("subtitle")

        labelViewScrollContainer.styleClass.addAll("group-creation__label-container")
        groupFilterContainer.styleClass.add("group-creation__filters")

        dialogPane.buttonTypes.addAll(createButtonType, ButtonType.CANCEL)
        createButton = dialogPane.lookupButton(createButtonType) as Button
        cancelButton = dialogPane.lookupButton(ButtonType.CANCEL) as Button
        cancelButton.text = "cancel"
        createButton.styleClass.addAll("body", "group-creation__create-button")
        cancelButton.styleClass.addAll("body", "group-creation__cancel-button")

        title = "Create a Group"
        isResizable = true
        dialogPane.setMinSize(560.0, 400.0)
        /* end region styling */

        /* region view setup */
        setupLabelViewContainer()
        loadLabelViewContainer()
        setupGroupResultConverter()

        dialogPane.content = groupCreationContainer

        groupNameField.promptText = "group name..."
        groupFilterContainer.children.add(labelViewScrollContainer)
        groupCreationContainer.children.addAll(groupNameField, groupFilterLabel, groupFilterContainer)
        /* end region view setup */

        /* region event filters */
        // Prevent dialog from closing when input is invalid
        createButton.addEventFilter(
            ActionEvent.ACTION
        ) { event ->
            if (groupNameField.text.isBlank()) event.consume()
        }

        // Reset state on initialization
        onShowing = EventHandler {
            result = null
            groupNameField.text = ""
        }
        /* end region event filters */
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

    private fun loadLabelViewContainer(list: List<Label> = listOf()) {
        labelViewContainer.children.setAll(
            list.map {
                GroupCreationLabelView(sidepaneController = controller, label = it)
            }
        )
        labelViewContainer.children.add(AddGroupCreationLabelChip(controller = controller))
    }

    private fun setupGroupResultConverter() {
        setResultConverter { dialogButton ->

            when (dialogButton) {
                (createButtonType) -> {
                    group.name = groupNameField.text
                    controller.groupCreationLabelListProperty.value.forEach { label ->
                        if (label.name !in controller.labelListProperty.map { it.name }) {
                            controller.createLabel(false, label)
                        }
                    }
                    group.filter.labelIds = controller.labelListProperty.filter { it.name in controller.groupCreationLabelListProperty.value.map { groupCreationLabel -> groupCreationLabel.name } }.map { it.id }.toMutableList()
                    result = group
                }
                else -> result = null
            }

            controller.groupCreationLabelListProperty.setAll(mutableListOf())
            return@setResultConverter result
        }
    }
}
