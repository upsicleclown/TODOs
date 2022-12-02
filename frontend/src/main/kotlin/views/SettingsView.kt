package views

import cache.Cache
import controllers.GroupViewController
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.ScrollPane
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.layout.Priority
import javafx.scene.layout.Region
import javafx.scene.layout.VBox
import javafx.stage.Stage
import models.Label
import theme.Theme
import javafx.scene.control.Label as JfxLabel

class SettingsView(
    private val controller: GroupViewController,
    private val cache: Cache
) : Stage() {
    private val labelScrollContainer = ScrollPane()
    private val labelContent = VBox(8.0)
    private var labelTitle = JfxLabel("LABELS")
    private var themeTitle = JfxLabel("THEMES")
    private val darkModeButton = Button("dark")
    private val lightModeButton = Button("light")
    private val root = HBox(12.0)
    private val spacer = Region()
    private val labelContainer = VBox()
    private val themeContainer = VBox(12.0)

    init {
        title = "Settings"
        minWidth = 400.0

        /* region styling */
        root.styleClass.add("settings")
        HBox.setHgrow(spacer, Priority.ALWAYS)
        root.style = Theme.stylesForTheme(cache.getWindowSettings().theme)
        cache.themeChangeProperty.addListener { _, _, _ ->
            root.style = Theme.stylesForTheme(cache.getWindowSettings().theme)
        }
        labelTitle.styleClass.addAll("labels__title", "h2")
        labelContainer.alignment = Pos.TOP_CENTER
        themeTitle.styleClass.addAll("themes__title", "h2")
        themeContainer.alignment = Pos.TOP_CENTER
        lightModeButton.styleClass.addAll("body", "themes__button")
        darkModeButton.styleClass.addAll("body", "themes__button")
        /* end region styling */

        /* region view setup */
        labelScrollContainer.content = labelContent
        labelContainer.children.addAll(labelTitle, labelScrollContainer)
        themeContainer.children.addAll(themeTitle, darkModeButton, lightModeButton)
        root.children.addAll(labelContainer, spacer, themeContainer)
        /* end region view setup */

        /* region event filters */
        darkModeButton.setOnAction {
            setTheme(Theme.DARK)
        }

        lightModeButton.setOnAction {
            setTheme(Theme.LIGHT)
        }

        controller.labelListProperty.addListener { _, _, _ ->
            var labelChips = listOf<BorderPane>()
            val itemLabels: List<Label> = controller.labels()
            if (itemLabels.isNotEmpty()) {
                labelChips = itemLabels.map {
                    SettingsLabelView(groupController = controller, label = it)
                }
            }

            labelContent.children.clear()
            if (labelChips.isNotEmpty()) {
                labelContent.children.addAll(labelChips)
            }
            labelContent.children.add(AddSettingsLabelChip(controller = controller))
        }
        /* end region event filters */

        val settingsScene = Scene(root, 400.0, 400.0)
        this.scene = settingsScene
        this.scene.stylesheets.add("/style/TODOApplication.css")
    }

    private fun setTheme(theme: Theme) {
        var windowSettings = cache.getWindowSettings()
        windowSettings.theme = theme
        cache.editWindowSettings(
            windowSettings
        )
        cache.saveWindowSettings()
        cache.themeChangeProperty.set(!cache.themeChangeProperty.value)
    }
}
