package commands

/**
 * Command executed when the provided command-line argument don't map to a supported behaviour.
 */
class ErrorCommand(private val args: List<String>) : Command {

    companion object {
        private const val red = "\u001B[31m"
        const val reset = "\u001B[0m"

        fun print(message: String) {
            println(red + message + reset)
            println()
        }
    }

    override fun execute() {
        print("Unsupported arguments: $args. Please run the '--help' command for details on how to use this program.")
    }
}
