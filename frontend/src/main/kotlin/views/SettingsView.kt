package views

import controllers.GroupViewController
import javafx.scene.Scene
import javafx.scene.control.ScrollPane
import javafx.scene.layout.BorderPane
import javafx.scene.layout.VBox
import javafx.scene.text.Text
import javafx.stage.Stage
import models.Label

class SettingsView(
    private val controller: GroupViewController
) : Stage() {
    private val settingsViewScrollContainer = ScrollPane()
    private val settingsViewContainer = VBox()
    private var labelTitle = Text("Labels")

    init {
        title = "Settings"

        val root = VBox()
        /* region styling */
        root.styleClass.add("settings")
        labelTitle.styleClass.addAll("settings__title", "h2")
        /* end region styling */

        root.children.add(labelTitle)

        root.children.add(settingsViewScrollContainer)
        settingsViewScrollContainer.content = settingsViewContainer

        controller.labelListProperty.addListener { _, _, _ ->
            var labelChips = listOf<BorderPane>()
            val itemLabels: List<Label> = controller.labels()
            if (itemLabels.isNotEmpty()) {
                labelChips = itemLabels.map {
                    SettingsLabelView(groupController = controller, label = it)
                }
            }

            settingsViewContainer.children.clear()
            if (labelChips.isNotEmpty()) {
                settingsViewContainer.children.addAll(labelChips)
            }
        }

        val settingsScene = Scene(root, 400.0, 400.0)
        this.scene = settingsScene
        this.scene.stylesheets.add("/style/TODOApplication.css")
    }
}
