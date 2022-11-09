package views

import controllers.GroupViewController
import javafx.scene.control.Button
import javafx.scene.layout.BorderPane
import javafx.scene.layout.Region
import models.Item
import models.Label
import javafx.scene.control.Label as JfxLabel

class LabelView(private val groupController: GroupViewController, private val label: Label, private val item: Item) : BorderPane() {
    private val labelText = JfxLabel(label.name)
    private val deleteButton = Button("x")

    init {
        /* region styling */
        styleClass.add("item__label-chip")
        maxHeight = LABEL_HEIGHT

        labelText.styleClass.addAll("label-max", "item__label-chip__text")
        labelText.minWidth = Region.USE_PREF_SIZE // do not crush text as we start to overflow the scroll pane

        deleteButton.styleClass.addAll("label-max", "item__label-chip__delete-button")
        /* end region */

        /* region event filters */
        deleteButton.setOnAction {
            val originalItem = item.copy()
            val newItem = item.copy()
            newItem.labelIds.remove(label.id)

            groupController.editItem(newItem, originalItem)
        }
        /* end region */

        top = null
        center = labelText
        right = deleteButton
        left = null
    }

    companion object {
        const val LABEL_HEIGHT = 32.0
        const val DEFAULT_LABEL_COLOR = "#89CFF0"
    }
}
