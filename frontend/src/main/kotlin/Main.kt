import javafx.application.Application

/**
 * The sole purpose of this main class is to call our application class (which extends javafx.application.Application.)
 * This way, the Java launcher doesn't require the javafx libraries to be available as named modules.
 *
 * Issue with JavaFx: https://github.com/javafxports/openjdk-jfx/issues/236
 * Solution inspired from: https://stackoverflow.com/questions/25873769/launch-javafx-application-from-another-class
 */
object Main {
    @JvmStatic
    fun main(args: Array<String>) {
        Application.launch(TODOApplication::class.java)
    }
}
