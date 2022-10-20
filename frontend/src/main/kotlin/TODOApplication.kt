import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.layout.GridPane
import javafx.stage.Stage
import javafx.stage.WindowEvent
import models.WindowSettings
import ui.Cache
import ui.controllers.GroupViewController
import ui.controllers.SidepaneController
import ui.views.GroupView
import ui.views.SidepaneView
import java.beans.EventHandler

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

        val scene = Scene(root, cache.getWindowSettings().width, cache.getWindowSettings().height)

        stage.title = "TODO List"
        stage.scene = scene
        stage.isResizable = true
        stage.minHeight = 500.0
        stage.minWidth = 500.0
        stage.show()
    }

    override fun stop() {
        super.stop()
        cache.editWindowSettings(WindowSettings(primaryStage.scene.height, primaryStage.scene.width))
        println(primaryStage.scene.width)
        println(primaryStage.scene.height)
    }

}
