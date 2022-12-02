package views

import cache.Cache
import controllers.GroupViewController
import javafx.beans.value.ChangeListener
import javafx.collections.ObservableList
import javafx.event.EventHandler
import javafx.geometry.Pos
import javafx.scene.Node
import javafx.scene.control.Alert
import javafx.scene.control.Button
import javafx.scene.control.ButtonType
import javafx.scene.control.CheckBox
import javafx.scene.control.ComboBox
import javafx.scene.control.Label
import javafx.scene.control.RadioButton
import javafx.scene.control.ScrollPane
import javafx.scene.control.TextField
import javafx.scene.control.ToggleGroup
import javafx.scene.input.MouseEvent
import javafx.scene.layout.HBox
import javafx.scene.layout.Region
import javafx.scene.layout.VBox
import jfxtras.scene.control.LocalDateTimeTextField
import models.BooleanOperator
import models.Item
import models.Priority
import java.util.Optional
import kotlin.properties.Delegates

class GroupView(private val controller: GroupViewController, private val cache: Cache) : VBox(36.0) {

    enum class Attribute(val value: String) {
        IS_COMPLETED("completed"), EDT_DUEDATE("due date"), PRIORITY("priority"), CUSTOM("custom");

        companion object {
            private val map = values().associateBy { it.value }
            infix fun fromValue(value: String) = map[value]
        }
    }

    data class SortOrder(var attribute: Attribute = Attribute.IS_COMPLETED, var isDesc: Boolean = false)

    var sortOrder: SortOrder by Delegates.observable(SortOrder()) { _, _, _ -> loadSortOrderPicker() }
    private val sortOrderAttributePicker = ComboBox<String>()
    private val sortOrderIsDescButton = Button()
    private val sortOrderPicker = HBox(12.0)
    private val sortOrderLabel = Label("SORT")
    private val dateRangeFilterPickerContainer = HBox(20.0)
    private val completionFilterPickerContainer = HBox(20.0)
    private val completeFilterButton = RadioButton("COMPLETE")
    private val incompleteFilterButton = RadioButton("INCOMPLETE")
    private val clearButton = Button("CLEAR")
    private val labelViewFilterScrollContainer = ScrollPane()
    private val labelViewFilterContainer = HBox(20.0)
    private val labelFilterBooleanOperatorPicker = ComboBox<BooleanOperator>()
    private val labelFilterContainer = HBox(20.0)
    private val priorityFilterPickerContainer = HBox(20.0)
    private val priorityFilterPickerLabel = Label("PRIORITIES")
    private val edtStartDateFilterPicker = LocalDateTimeTextField()
    private val edtEndDateFilterPicker = LocalDateTimeTextField()
    private val groupFilterLabel = Label("FILTERS")
    private val groupFilterContainer = VBox(15.0)
    private val filterSortContainer = HBox()
    private val itemListScrollContainer = ScrollPane()
    private val itemListContainer = VBox(36.0)
    private val itemCreationField = TextField()
    private var currentGroupName = Label("All Items")
    private var logoutButton = Button("logout")
    private var logoutButtonContainer = HBox()

    init {
        /* region styling */
        styleClass.add("group")
        currentGroupName.styleClass.addAll("group__title", "title-max")
        itemCreationField.styleClass.addAll("h2", "group__create-item")
        itemListScrollContainer.styleClass.addAll("group__item-list__container")
        itemListContainer.styleClass.addAll("group__item-list")
        logoutButton.styleClass.addAll("group__cancel-button", "body")
        logoutButton.minWidth = 125.0 /* prevent crunching logout button text */
        /* end region styling */

        /* region event filters */
        logoutButton.setOnAction {
            logoutUser()
        }

        // when enter is pressed
        itemCreationField.setOnAction {
            controller.createItem(Item(itemCreationField.text, false))
            itemCreationField.text = ""
        }

        // when clicking outside of an item, unfocus that item
        itemListContainer.addEventHandler(
            MouseEvent.MOUSE_CLICKED,
            EventHandler<MouseEvent> {
                requestFocus()
                controller.clearFocus()
            }
        )

        this.addEventHandler(
            MouseEvent.MOUSE_CLICKED,
            EventHandler<MouseEvent> {
                requestFocus()
                controller.clearFocus()
            }
        )
        /* end region event filters */

        /* region view setup */
        logoutButtonContainer.children.add(logoutButton)
        logoutButtonContainer.alignment = Pos.BASELINE_RIGHT

        setUpSortOrderPicker()
        filterSortContainer.children.setAll(sortOrderPicker)

        itemCreationField.promptText = "Create a new item..."

        itemListScrollContainer.isFitToWidth = true
        itemListScrollContainer.content = itemListContainer
        itemListScrollContainer.hmax = 0.0
        itemListScrollContainer.hbarPolicy = ScrollPane.ScrollBarPolicy.NEVER

        filterSortContainer.spacing = 30.0
        val headerSpacer = Region()
        val header = HBox(currentGroupName, headerSpacer, logoutButtonContainer)
        HBox.setHgrow(headerSpacer, javafx.scene.layout.Priority.ALWAYS)
        children.addAll(header, filterSortContainer, itemCreationField, itemListScrollContainer)
        /* end region view setup */

        /* region data bindings */
        controller.currentGroupProperty.addListener { _, _, newGroup ->
            if (newGroup != null) {
                currentGroupName.text = newGroup.name
                setUpFilterPicker()

                groupFilterLabel.styleClass.addAll("h1", "group__filters__label")
                val filterSortContainerSpacer = Region()
                val groupFilterParentContainer = HBox(8.0)
                groupFilterParentContainer.children.setAll(
                    groupFilterLabel,
                    filterSortContainerSpacer,
                    groupFilterContainer
                )
                HBox.setHgrow(filterSortContainerSpacer, javafx.scene.layout.Priority.ALWAYS)
                filterSortContainer.children.setAll(sortOrderPicker, groupFilterParentContainer)
            } else {
                currentGroupName.text = "All Items"
                filterSortContainer.children.setAll(sortOrderPicker)
            }
        }

        controller.displayItemListProperty.addListener { _, _, newItemList ->
            itemListContainer.children.setAll(
                newItemList.map {
                        item ->
                    ItemView(controller, item, cache)
                }
            )
        }

        // reload the items if the labels are modified
        controller.labelListProperty.addListener { _, _, _ ->
            itemListContainer.children.setAll(
                controller.displayItemList.map {
                        item ->
                    ItemView(controller, item, cache)
                }
            )
        }
        /* end region data bindings */
    }

    private fun logoutUser() {
        val alert = Alert(Alert.AlertType.CONFIRMATION)
        alert.title = "Confirmation Dialog"
        alert.headerText = "Are you sure you want to logout?"

        val result: Optional<ButtonType> = alert.showAndWait()
        if (result.get() === ButtonType.OK) {
            controller.logoutUser()
        } else {
            // do nothing
        }
    }

    private fun loadSortOrderPicker() {
        sortOrderIsDescButton.text = if (sortOrder.isDesc) "↑" else "↓"
        sortOrderAttributePicker.value = sortOrder.attribute.value
    }

    private fun setUpSortOrderPicker() {
        /* region styling */
        sortOrderPicker.styleClass.addAll("sort")
        sortOrderLabel.styleClass.addAll("sort__label", "h1")
        sortOrderAttributePicker.styleClass.addAll("label-max", "sort__picker")
        sortOrderIsDescButton.styleClass.addAll("label-max", "sort__desc-button")
        /* end region styling */

        loadSortOrderPicker()

        sortOrderIsDescButton.setOnAction {
            val newIsDesc = !sortOrder.isDesc
            controller.editSortOrder(SortOrder(sortOrder.attribute, newIsDesc), sortOrder)
        }

        sortOrderAttributePicker.items.addAll(Attribute.values().map { it.value })

        sortOrderAttributePicker.valueProperty().addListener { _, oldValue, newValue ->
            if (oldValue != newValue) {
                controller.editSortOrder(SortOrder(Attribute.fromValue(newValue)!!, sortOrder.isDesc), sortOrder)
                // disable IsDescButton on custom ordering.
                sortOrderIsDescButton.isDisable = newValue == Attribute.CUSTOM.value
            }
        }

        sortOrderPicker.children.setAll(sortOrderLabel, sortOrderAttributePicker, sortOrderIsDescButton)
    }

    private fun setUpFilterPicker() {
        groupFilterContainer.children.setAll(
            priorityFilterPickerContainer,
            completionFilterPickerContainer,
            dateRangeFilterPickerContainer,
            labelFilterContainer
        )
        setupPriorityFilterPicker()
        setupCompletionPicker()
        setUpDateRangePicker()
        setupGroupFilterLabelContainer()
        loadGroupFilterLabelViewContainer()
    }

    private fun setupPriorityFilterPicker() {
        priorityFilterPickerContainer.styleClass.addAll("group__priority-picker")
        priorityFilterPickerLabel.styleClass.addAll("group__priority-picker__label", "label-max")

        val priorityBoxes = Priority.values().map {
            val checkBox = CheckBox(it.name)
            checkBox.styleClass.addAll("group__priority-picker__check-box","label-min")
            checkBox
        }
        priorityBoxes.forEach { checkbox ->
            if (Priority.valueOf(checkbox.text) in controller.currentGroupProperty.value.filter.priorities) {
                checkbox.isSelected = true
            }
        }

        priorityBoxes.forEach { checkbox ->
            checkbox.selectedProperty().addListener(
                ChangeListener { _, _, isSelected ->
                    controller.currentGroupProperty.value?.let {
                        val newFilter = controller.currentGroupProperty.value.filter.copy()
                        if (isSelected) {
                            newFilter.priorities.add(Priority.valueOf(checkbox.text))
                        } else {
                            newFilter.priorities.remove(Priority.valueOf(checkbox.text))
                        }
                        controller.editCurrentGroupFilter(newFilter)
                    }
                }
            )
        }
        priorityFilterPickerContainer.children.setAll(listOf(priorityFilterPickerLabel) + priorityBoxes)
    }

    private fun setupCompletionPicker() {
        val group = ToggleGroup()
        completeFilterButton.styleClass.addAll("group__completion-picker__complete-button", "label-max")
        completeFilterButton.toggleGroup = group
        incompleteFilterButton.styleClass.addAll("group__completion-picker__incomplete-button", "label-max")
        incompleteFilterButton.toggleGroup = group
        clearButton.styleClass.addAll("group__completion-picker__clear-button", "label-max")

        group.selectedToggleProperty().addListener(
            ChangeListener { _, _, toggle ->
                val newFilter = controller.currentGroupProperty.value.filter.copy()
                newFilter.isCompleted = toggle?.equals(completeFilterButton)
                controller.editCurrentGroupFilter(newFilter)
            }
        )

        clearButton.setOnAction {
            completeFilterButton.isSelected = false
            incompleteFilterButton.isSelected = false
            val newFilter = controller.currentGroupProperty.value.filter.copy()
            newFilter.isCompleted = null
            controller.editCurrentGroupFilter(newFilter)
        }

        controller.currentGroupProperty.value.filter.isCompleted?.let {
            completeFilterButton.isSelected = it
            incompleteFilterButton.isSelected = !it
        }

        completionFilterPickerContainer.styleClass.addAll("group__completion-picker")
        completionFilterPickerContainer.children.setAll(completeFilterButton, incompleteFilterButton, clearButton)
    }

    private fun setUpDateRangePicker() {
        val dateRangeLabel = Label("TO")
        dateRangeLabel.styleClass.addAll("group__date-picker__label", "label-max")
        dateRangeFilterPickerContainer.children.setAll(edtStartDateFilterPicker, dateRangeLabel, edtEndDateFilterPicker)

        edtStartDateFilterPicker.localDateTime = controller.currentGroupProperty.value.filter.edtStartDateRange
        edtStartDateFilterPicker.styleClass.setAll("date-picker", "label-max")
        edtEndDateFilterPicker.localDateTime = controller.currentGroupProperty.value.filter.edtEndDateRange
        edtEndDateFilterPicker.styleClass.setAll("date-picker", "label-max")

        edtStartDateFilterPicker.localDateTimeProperty().addListener(
            ChangeListener { _, _, newValue ->
                edtEndDateFilterPicker.localDateTime?.let {
                    if (newValue == null || newValue > it) {
                        edtStartDateFilterPicker.localDateTime = null
                    }
                }
                val newFilter = controller.currentGroupProperty.value.filter.copy()
                newFilter.edtStartDateRange = edtStartDateFilterPicker.localDateTime
                controller.editCurrentGroupFilter(newFilter)
            }
        )

        edtEndDateFilterPicker.localDateTimeProperty().addListener(
            ChangeListener { _, _, newValue ->
                edtStartDateFilterPicker.localDateTime?.let {
                    if (newValue == null || newValue < it) {
                        edtEndDateFilterPicker.localDateTime = null
                    }
                }
                val newFilter = controller.currentGroupProperty.value.filter.copy()
                newFilter.edtEndDateRange = edtEndDateFilterPicker.localDateTime
                controller.editCurrentGroupFilter(newFilter)
            }
        )
    }

    fun setupGroupFilterLabelContainer() {
        labelFilterBooleanOperatorPicker.items.setAll(*BooleanOperator.values())
        labelFilterBooleanOperatorPicker.value = controller.currentGroupProperty.value.filter.labelBooleanOperator
        labelFilterBooleanOperatorPicker.styleClass.addAll("sort", "sort__picker", "label-max")

        labelFilterBooleanOperatorPicker.valueProperty().addListener(
            ChangeListener { _, oldValue, newValue ->
                if (oldValue != newValue && newValue != null) {
                    val newFilter = controller.currentGroupProperty.value.filter.copy()
                    newFilter.labelBooleanOperator = newValue
                    controller.editCurrentGroupFilter(newFilter)
                }
            }
        )

        labelViewFilterScrollContainer.isFitToHeight = true
        labelViewFilterScrollContainer.prefHeight = 62.0
        labelViewFilterScrollContainer.vbarPolicy = ScrollPane.ScrollBarPolicy.NEVER // hide vertical scroll bar
        labelViewFilterScrollContainer.vmax = 0.0 // prevent vertical scrolling
        labelViewFilterScrollContainer.content = labelViewFilterContainer

        labelFilterContainer.children.setAll(labelFilterBooleanOperatorPicker, labelViewFilterScrollContainer)

        controller.groupFilterLabelListProperty.value.setAll(controller.currentGroupProperty.value.filter.labelIds)
        controller.groupFilterLabelListProperty.addListener(
            ChangeListener { _, _, _ ->
                loadGroupFilterLabelViewContainer()
            }
        )
        controller.labelListProperty.addListener(
            ChangeListener { _, _, _ ->
                loadGroupFilterLabelViewContainer()
            }
        )
    }

    fun loadGroupFilterLabelViewContainer() {
        labelViewFilterContainer.children.setAll(
            controller.currentGroupProperty.value.filter.labelIds.map { labelId ->
                val label = controller.labels().first { it.id == labelId }
                GroupFilterLabelView(controller = controller, label = label)
            }
        )
        labelViewFilterContainer.children.add(AddGroupFilterLabelChip(controller = controller))
    }

    fun getItemContainers(): ObservableList<Node>? {
        return itemListContainer.children
    }

    fun setCustomOrder() {
        sortOrderAttributePicker.value = Attribute.CUSTOM.value
    }
}
