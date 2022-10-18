import controllers.GroupViewController
import controllers.SidepaneController
import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.layout.GridPane
import javafx.stage.Stage
import views.GroupView
import views.SidepaneView

class TODOApplication : Application() {

    public val sidepaneController = SidepaneController(this)
    public val groupViewController = GroupViewController()
    public val sidepaneView = SidepaneView(sidepaneController)
    public val groupView = GroupView(groupViewController)

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

    override fun start(primaryStage: Stage) {
        val root = MainView(sidepaneView, groupView)

        val scene = Scene(root, 500.0, 500.0)
        primaryStage.title = "TODO List"
        primaryStage.scene = scene
        primaryStage.isResizable = true
        primaryStage.minHeight = 500.0
        primaryStage.minWidth = 500.0
        primaryStage.show()
    }
}
