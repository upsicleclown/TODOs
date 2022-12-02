import cache.Cache
import client.TODOClient
import clipboard.Clipboard
import commands.CommandHandler
import commands.LogOutUserCommand
import controllers.GroupViewController
import controllers.SidepaneController
import javafx.application.Application
import javafx.event.EventHandler
import javafx.scene.Cursor
import javafx.scene.Scene
import javafx.scene.control.Menu
import javafx.scene.control.MenuBar
import javafx.scene.control.MenuItem
import javafx.scene.control.SeparatorMenuItem
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyCodeCombination
import javafx.scene.input.KeyCombination
import javafx.scene.input.KeyEvent
import javafx.scene.layout.BorderPane
import javafx.stage.Stage
import models.Item
import models.WindowSettings
import theme.Theme
import views.GroupView
import views.LoginView
import views.SettingsView
import views.SidepaneView

class TODOApplication : Application() {
    private var primaryStage = Stage()

    val commandHandler = CommandHandler()
    private val cache = Cache()
    private val clipboard = Clipboard()
    private val minSize = 660.0

    private val sidepaneController = SidepaneController(this)
    val groupViewController = GroupViewController(this, cache)
    private val todoClient = TODOClient
    private val sidepaneView = SidepaneView(sidepaneController, cache)
    private val groupView = GroupView(groupViewController, cache)
    private val settingsView = SettingsView(groupViewController, cache)

    init {
        sidepaneController.addView(sidepaneView)
        groupViewController.addView(groupView)
    }
    class MainView(sidepaneView: SidepaneView, groupView: GroupView) : BorderPane() {
        init {
            this.left = sidepaneView
            this.center = groupView

            styleClass.add("main-view")
        }
    }

    /**
     * Changes the scene to be the main view and refreshes both the side pane and group view.
     * Also adds the menu bar.
     */
    fun setMainView() {
        todoClient.init()

        val root = MainView(sidepaneView, groupView)

        val menuBar = MenuBar()

        val fileMenu = Menu("File")
        val createItem = MenuItem("Create Item (⌘N)")
        createItem.setOnAction { createItem() }
        fileMenu.items.add(createItem)

        val editMenu = Menu("Edit")
        val undo = MenuItem("Undo (⌘Z)")
        val redo = MenuItem("Redo (⌘Y)")
        val cut = MenuItem("Cut (⌘X)")
        val copy = MenuItem("Copy (⌘C)")
        val paste = MenuItem("Paste (⌘P)")
        undo.setOnAction { commandHandler.undo() }
        redo.setOnAction { commandHandler.redo() }
        cut.setOnAction { cutItem() }
        copy.setOnAction { copyItem() }
        paste.setOnAction { pasteItem() }
        editMenu.items.addAll(undo, redo, SeparatorMenuItem(), cut, copy, paste)

        val settingsMenu = Menu("Settings")
        val openSettings = MenuItem("Open Settings (⌘,)")
        openSettings.setOnAction { settingsView.show() }
        settingsMenu.items.add(openSettings)

        menuBar.menus.addAll(fileMenu, editMenu, settingsMenu)
        root.top = menuBar
        root.style = Theme.stylesForTheme(cache.getWindowSettings().theme)
        cache.themeChangeProperty.addListener { _, _, _ ->
            root.style = Theme.stylesForTheme(cache.getWindowSettings().theme)
        }
        primaryStage.scene.root = root

        enableHotkeys(primaryStage.scene)
    }

    fun setLoginView() {
        val root = LoginView(this)
        root.style = Theme.stylesForTheme(cache.getWindowSettings().theme)
        primaryStage.scene.root = root
    }

    override fun start(stage: Stage) {
        primaryStage = stage
        val root = LoginView(this)
        root.style = Theme.stylesForTheme(cache.getWindowSettings().theme)

        val scene = Scene(root)
        scene.stylesheets.add("/style/TODOApplication.css")

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
        groupViewController.saveCurrentSortOrderIfNeeded()
        cache.saveGroupToItemOrdering()
        cache.saveUserToItemOrdering()
        cache.editWindowSettings(
            WindowSettings(
                primaryStage.x,
                primaryStage.y,
                primaryStage.height,
                primaryStage.width,
                cache.getWindowSettings().theme
            )
        )
        cache.saveWindowSettings()
        commandHandler.execute(LogOutUserCommand())
    }

    /**
     * Adds hotkeys to scene
     */
    fun enableHotkeys(scene: Scene) {
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
                val commandComma: KeyCombination = KeyCodeCombination(
                    KeyCode.COMMA,
                    KeyCombination.SHORTCUT_DOWN
                )

                override fun handle(event: KeyEvent?) {
                    if (commandN.match(event)) {
                        createItem()
                    } else if (commandC.match(event)) {
                        copyItem()
                    } else if (commandX.match(event)) {
                        cutItem()
                    } else if (commandV.match(event)) {
                        pasteItem()
                    } else if (commandZ.match(event)) {
                        commandHandler.undo()
                    } else if (commandY.match(event)) {
                        commandHandler.redo()
                    } else if (commandComma.match(event)) {
                        settingsView.show()
                    } else if (event?.code == KeyCode.UP) {
                        moveItem(downwards = false)
                    } else if (event?.code == KeyCode.DOWN) {
                        moveItem(downwards = true)
                    } else {
                        // Don't consume event if not one of the above.
                        return
                    }
                    event?.consume()
                }
            }
        )
    }

    /**
     * Hotkey / menu functions
     */
    fun createItem() {
        groupViewController.createItem(Item("Untitled", false))
    }

    fun copyItem() {
        val item: Item? = groupViewController.focusedItemProperty.value
        if (item != null) {
            clipboard.saveItem(item)
        }
    }

    fun cutItem() {
        val item: Item? = groupViewController.focusedItemProperty.value
        if (item != null) {
            groupViewController.deleteItem(item)
            clipboard.saveItem(item)
        }
    }

    fun pasteItem() {
        val item: Item? = clipboard.getSavedItem()
        if (item != null) {
            groupViewController.createItem(item)
        }
    }

    fun moveItem(downwards: Boolean) {
        val item: Item? = groupViewController.focusedItemProperty.value
        if (item != null) {
            groupViewController.moveItem(item, downwards)
        }
    }

    /**
     * Cursor functions
     */
    fun setOpenHandCursor() {
        primaryStage.scene.cursor = Cursor.OPEN_HAND
    }

    fun setClosedHandCursor() {
        primaryStage.scene.cursor = Cursor.CLOSED_HAND
    }

    fun resetCursor() {
        primaryStage.scene.cursor = Cursor.DEFAULT
    }
}
