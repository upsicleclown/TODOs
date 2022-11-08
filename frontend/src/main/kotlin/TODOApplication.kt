import cache.Cache
import clipboard.Clipboard
import commands.CommandHandler
import controllers.GroupViewController
import controllers.SidepaneController
import javafx.application.Application
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyCodeCombination
import javafx.scene.input.KeyCombination
import javafx.scene.input.KeyEvent
import javafx.scene.layout.BorderPane
import javafx.stage.Stage
import models.Item
import models.WindowSettings
import views.GroupView
import views.LogInView
import views.SidepaneView

class TODOApplication : Application() {
    private var primaryStage = Stage()
    private val sidepaneController = SidepaneController(this)
    val groupViewController = GroupViewController(this)
    private val sidepaneView = SidepaneView(sidepaneController)
    private val groupView = GroupView(groupViewController)

    val commandHandler = CommandHandler()
    private val cache = Cache()
    private val clipboard = Clipboard()
    private val minSize = 500.0

    init {
        sidepaneController.addView(sidepaneView)
        groupViewController.addView(groupView)
    }
    class MainView(sidepaneView: SidepaneView, groupView: GroupView) : BorderPane() {
        init {
            this.left = sidepaneView
            this.center = groupView
        }
    }

    /**
     * Changes the scene to be the main view and refreshes both the side pane and group view.
     */
    fun setMainView() {
        sidepaneController.refreshGroups()
        groupViewController.reloadGroupView()
        val root = MainView(sidepaneView, groupView)
        val scene = Scene(root)
        addHotkeysToMainViewScene(scene)
        primaryStage.scene = scene
    }

    override fun start(stage: Stage) {
        primaryStage = stage
        val root = LogInView(this)

        val scene = Scene(root)

        stage.title = "TODO List"
        stage.scene = scene
        stage.isResizable = true
        stage.x = cache.getWindowSettings().x
        stage.y = cache.getWindowSettings().y
        stage.width = cache.getWindowSettings().width
        stage.height = cache.getWindowSettings().height
        stage.minHeight = minSize
        stage.minWidth = minSize
        stage.show()
    }

    override fun stop() {
        super.stop()
        cache.editWindowSettings(WindowSettings(primaryStage.x, primaryStage.y, primaryStage.height, primaryStage.width))
        cache.saveWindowSettings()
    }

    /**
     * Adds hotkeys to the main view scene
     */
    private fun addHotkeysToMainViewScene(scene: Scene) {
        scene.addEventFilter(
            KeyEvent.KEY_PRESSED,
            object : EventHandler<KeyEvent?> {
                val commandN: KeyCombination = KeyCodeCombination(
                    KeyCode.N,
                    KeyCombination.SHORTCUT_DOWN
                )
                val commandC: KeyCombination = KeyCodeCombination(
                    KeyCode.C,
                    KeyCombination.SHORTCUT_DOWN
                )
                val commandX: KeyCombination = KeyCodeCombination(
                    KeyCode.X,
                    KeyCombination.SHORTCUT_DOWN
                )
                val commandV: KeyCombination = KeyCodeCombination(
                    KeyCode.V,
                    KeyCombination.SHORTCUT_DOWN
                )
                val commandZ: KeyCombination = KeyCodeCombination(
                    KeyCode.Z,
                    KeyCombination.SHORTCUT_DOWN
                )
                val commandY: KeyCombination = KeyCodeCombination(
                    KeyCode.Y,
                    KeyCombination.SHORTCUT_DOWN
                )

                override fun handle(event: KeyEvent?) {
                    if (commandN.match(event)) {
                        groupViewController.createItem(Item("Untitled", false))
                    } else if (commandC.match(event)) {
                        val item: Item? = groupViewController.getFocusedItem()
                        if (item != null) {
                            clipboard.saveItem(item)
                        }
                    } else if (commandX.match(event)) {
                        val item: Item? = groupViewController.getFocusedItem()
                        if (item != null) {
                            groupViewController.deleteItem(item)
                            clipboard.saveItem(item)
                        }
                    } else if (commandV.match(event)) {
                        val item: Item? = clipboard.getSavedItem()
                        if (item != null) {
                            groupViewController.createItem(item)
                        }
                    } else if (commandZ.match(event)) {
                        commandHandler.undo()
                    } else if (commandY.match(event)) {
                        commandHandler.redo()
                    } else {
                        // Don't consume event if not one of the above.
                        return
                    }
                    event?.consume()
                }
            }
        )
    }
}
