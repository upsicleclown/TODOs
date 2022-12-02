package views

import cache.Cache
import controllers.SidepaneController
import javafx.beans.value.ChangeListener
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.geometry.Pos
import javafx.scene.control.Button
import javafx.scene.control.ButtonBar
import javafx.scene.control.ButtonType
import javafx.scene.control.CheckBox
import javafx.scene.control.ComboBox
import javafx.scene.control.Dialog
import javafx.scene.control.RadioButton
import javafx.scene.control.ScrollPane
import javafx.scene.control.TextField
import javafx.scene.control.ToggleGroup
import javafx.scene.input.MouseEvent
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import jfxtras.scene.control.LocalDateTimeTextField
import models.BooleanOperator
import models.Filter
import models.Group
import models.Label
import models.Priority
import theme.Theme
import javafx.scene.control.Label as JfxLabel

class GroupCreationView(private val controller: SidepaneController, private val cache: Cache) : Dialog<Group?>() {
    private val DEFAULT_BOOLEAN_OPERATOR = BooleanOperator.AND
    var group = Group("", Filter(labelBooleanOperator = DEFAULT_BOOLEAN_OPERATOR))
    private var createButton = Button()
    private var cancelButton = Button()
    private val dateRangePickerContainer = HBox(20.0)
    private val completionPickerContainer = HBox(20.0)
    private val completeButton = RadioButton("COMPLETE")
    private val incompleteButton = RadioButton("INCOMPLETE")
    private val clearButton = Button("CLEAR")
    private val labelViewScrollContainer = ScrollPane()
    private val labelViewContainer = HBox(20.0)
    private val labelBooleanOperatorPicker = ComboBox<BooleanOperator>()
    private val labelContainer = HBox(20.0)
    private val priorityPickerContainer = HBox(20.0)
    private val priorityPickerLabel = JfxLabel("PRIORITIES")
    private val edtStartDatePicker = LocalDateTimeTextField()
    private val edtEndDatePicker = LocalDateTimeTextField()
    private val groupCreationContainer = VBox(24.0)
    private val groupNameField = TextField("")
    private val groupFilterLabel = JfxLabel("FILTERS")
    private val groupFilterContainer = VBox(15.0)
    private val createButtonType = ButtonType("save", ButtonBar.ButtonData.YES)

    init {
        /* region styling */
        dialogPane.scene.stylesheets.add("/style/TODOApplication.css")
        dialogPane.scene.root.style = Theme.stylesForTheme(cache.getWindowSettings().theme)

        dialogPane.scene.root.styleClass.add("group")
        groupCreationContainer.alignment = Pos.CENTER
        groupNameField.styleClass.addAll("title-min", "group__text-field")
        groupNameField.maxWidth = 540.0


        labelViewScrollContainer.styleClass.addAll("group__label-container")
        groupFilterContainer.styleClass.add("group__filters")
        groupFilterLabel.styleClass.addAll("subtitle", "group__filters__label")

        dialogPane.buttonTypes.addAll(createButtonType, ButtonType.CANCEL)
        createButton = dialogPane.lookupButton(createButtonType) as Button
        cancelButton = dialogPane.lookupButton(ButtonType.CANCEL) as Button
        cancelButton.text = "cancel"
        createButton.styleClass.addAll("body", "group__create-button")
        cancelButton.styleClass.addAll("body", "group__cancel-button")

        title = "Create a Group"
        isResizable = true
        dialogPane.setMinSize(560.0, 400.0)
        /* end region styling */

        /* region view setup */
        controller.todoApp()?.enableHotkeys(dialogPane.scene)
        setupPriorityPicker()
        setupCompletionPicker()
        setUpDateRangePicker()
        setupLabelContainer()
        loadLabelViewContainer()
        setupGroupResultConverter()

        dialogPane.content = groupCreationContainer

        groupNameField.promptText = "group name..."
        groupFilterContainer.children.addAll(
            priorityPickerContainer,
            completionPickerContainer,
            dateRangePickerContainer,
            labelContainer
        )
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

    private fun setupLabelContainer() {
        labelBooleanOperatorPicker.items.setAll(*BooleanOperator.values())
        labelBooleanOperatorPicker.value = DEFAULT_BOOLEAN_OPERATOR
        labelBooleanOperatorPicker.styleClass.addAll("sort", "sort__picker", "label-max")

        labelViewScrollContainer.isFitToHeight = true
        labelViewScrollContainer.prefHeight = 62.0
        labelViewScrollContainer.vbarPolicy = ScrollPane.ScrollBarPolicy.NEVER // hide vertical scroll bar
        labelViewScrollContainer.vmax = 0.0 // prevent vertical scrolling
        labelViewScrollContainer.content = labelViewContainer

        labelContainer.children.setAll(labelBooleanOperatorPicker, labelViewScrollContainer)

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

    private fun setupPriorityPicker() {
        priorityPickerContainer.styleClass.addAll("group__priority-picker")
        priorityPickerLabel.styleClass.addAll("group__priority-picker__label", "label-max")
        priorityPickerContainer.children.add(priorityPickerLabel)
        priorityPickerContainer.children.addAll(Priority.values().map {
            val checkBox = CheckBox(it.name)
            checkBox.styleClass.addAll("group__priority-picker__check-box","label-min")
            checkBox
        })
    }

    private fun setupCompletionPicker() {
        val group = ToggleGroup()
        completeButton.styleClass.addAll("group__completion-picker__complete-button", "label-max")
        completeButton.toggleGroup = group
        incompleteButton.styleClass.addAll("group__completion-picker__incomplete-button", "label-max")
        incompleteButton.toggleGroup = group
        clearButton.styleClass.addAll("group__completion-picker__clear-button", "label-max")

        clearButton.setOnAction {
            completeButton.isSelected = false
            incompleteButton.isSelected = false
        }

        completionPickerContainer.styleClass.addAll("group__completion-picker")
        completionPickerContainer.children.addAll(completeButton, incompleteButton, clearButton)
    }

    private fun setUpDateRangePicker() {
        val dateRangeLabel = JfxLabel("TO")
        dateRangeLabel.styleClass.addAll("group__date-picker__label", "label-max")
        dateRangePickerContainer.children.addAll(edtStartDatePicker, dateRangeLabel, edtEndDatePicker)

        edtStartDatePicker.localDateTimeProperty().addListener(
            ChangeListener { _, _, newValue ->
                edtEndDatePicker.localDateTime?.let {
                    if (newValue > it) {
                        edtStartDatePicker.localDateTime = null
                    }
                }
            }
        )
        edtStartDatePicker.styleClass.setAll("date-picker", "label-max")

        edtEndDatePicker.localDateTimeProperty().addListener(
            ChangeListener { _, _, newValue ->
                edtStartDatePicker.localDateTime?.let {
                    if (newValue < it) {
                        edtEndDatePicker.localDateTime = null
                    }
                }
            }
        )
        edtEndDatePicker.styleClass.setAll("date-picker", "label-max")
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
                    group.filter.labelBooleanOperator = labelBooleanOperatorPicker.value
                    group.filter.labelIds = controller.labelListProperty.filter { it.name in controller.groupCreationLabelListProperty.value.map { groupCreationLabel -> groupCreationLabel.name } }.map { it.id }.toMutableList()
                    group.filter.priorities.addAll((priorityPickerContainer.children.filter { it is CheckBox && it.isSelected } as List<CheckBox>).map { Priority.valueOf(it.text) })
                    group.filter.isCompleted = if (completeButton.isSelected == incompleteButton.isSelected) null else { completeButton.isSelected }
                    group.filter.edtStartDateRange = edtStartDatePicker.localDateTime
                    group.filter.edtEndDateRange = edtEndDatePicker.localDateTime
                    result = group
                }
                else -> result = null
            }

            controller.groupCreationLabelListProperty.setAll(mutableListOf())
            return@setResultConverter result
        }
    }
}
