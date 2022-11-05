import cache.Cache
import commands.CommandHandler
import controllers.GroupViewController
import controllers.SidepaneController
import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.layout.BorderPane
import javafx.stage.Stage
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
     * Changes the scene to be the main view and refreshed both the side pane and group view.
     */
    fun setMainView() {
        sidepaneView.refreshGroups()
        groupViewController.reloadGroupView()
        val root = MainView(sidepaneView, groupView)
        val scene = Scene(root)
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
}
