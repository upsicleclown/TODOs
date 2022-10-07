import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.layout.BorderPane
import javafx.scene.layout.VBox
import javafx.stage.Stage

class Main : Application() {

    override fun start(primaryStage: Stage) {
        val layout = BorderPane()
        layout.top = VBox()
        val scene = Scene(layout, 500.0, 500.0)
        primaryStage.title = "TODO List"
        primaryStage.scene = scene
        primaryStage.isResizable = true
        primaryStage.minHeight = 500.0
        primaryStage.minWidth = 500.0
        primaryStage.show()
    }
}
