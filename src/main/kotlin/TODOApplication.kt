import javafx.stage.Stage
import tornadofx.App
import tornadofx.launch;
import views.MainView

class TODOApplication : App(MainView::class) {
    override fun start(stage: Stage) {
        stage.isResizable = true
        super.start(stage)
    }
}

/*
    Entrypoint for TODO list Application
 */
fun main(args: Array<String>) {
    launch<TODOApplication>(args)
}
