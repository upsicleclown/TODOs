import controllers.GroupViewController
import controllers.SidepaneController
import javafx.stage.Stage
import tornadofx.App
import tornadofx.launch;
import views.MainView

class TODOApplication : App(MainView::class) {
    init {
        /* Init controllers required for startup */
        GroupViewController()
        SidepaneController()

    }
    override fun start(stage: Stage) {
        stage.isResizable = true
        stage.minWidth = 500.0
        stage.minHeight = 500.0
        super.start(stage)
    }
}

/*
    Entrypoint for TODO list Application
 */
fun main(args: Array<String>) {
    launch<TODOApplication>(args)
}
