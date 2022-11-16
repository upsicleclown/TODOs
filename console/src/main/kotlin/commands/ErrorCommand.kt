package commands

import models.User

/**
 * Command executed when the provided command-line argument don't map to a supported behaviour.
 */
class ErrorCommand(private val args: List<String>) : Command {

    companion object {
        private const val red = "\u001B[31m"
        private const val reset = "\u001B[0m"

        fun print(message: String) {
            println(red + message + reset)
            println()
        }
    }

    override fun execute(user: User?): User? {
        print("Unsupported arguments: $args. Please run the '--help' command for details on how to use this program.")
        return user
    }
}
