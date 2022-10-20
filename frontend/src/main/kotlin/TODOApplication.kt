import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.layout.GridPane
import javafx.stage.Stage
import models.WindowSettings
import cache.Cache
import controllers.GroupViewController
import controllers.SidepaneController
import views.GroupView
import views.SidepaneView

class TODOApplication : Application() {

    private var primaryStage = Stage()
    private val sidepaneController = SidepaneController(this)
    val groupViewController = GroupViewController()
    private val sidepaneView = SidepaneView(sidepaneController)
    private val groupView = GroupView(groupViewController)
    private val cache = Cache()

    init {
        sidepaneController.addView(sidepaneView)
        groupViewController.addView(groupView)
    }
    class MainView(sidepaneView: SidepaneView, groupView: GroupView) : GridPane() {
        init {
            this.add(sidepaneView, 0, 0)
            this.add(groupView, 1, 0)
        }
    }

    override fun start(stage: Stage) {
        primaryStage = stage
        val root = MainView(sidepaneView, groupView)

        val scene = Scene(root)

        stage.title = "TODO List"
        stage.scene = scene
        stage.isResizable = true
        stage.x = cache.getWindowSettings().x
        stage.y = cache.getWindowSettings().y
        stage.width = cache.getWindowSettings().width
        stage.height = cache.getWindowSettings().height
        stage.minHeight = 500.0
        stage.minWidth = 500.0
        stage.show()
    }

    override fun stop() {
        super.stop()
        cache.editWindowSettings(WindowSettings(primaryStage.x, primaryStage.y, primaryStage.height, primaryStage.width))
        cache.saveWindowSettings()
    }

}
