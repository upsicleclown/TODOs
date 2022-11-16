package commands

import models.User

/**
 * Command executed to provide details on how to use the program.
 */
class HelpCommand(private val args: List<String>) : Command {

    private val green = "\u001B[32m"
    private val reset = "\u001B[0m"

    private fun print(message: String) {
        println(green + message + reset)
    }

    override fun execute(user: User?): User? {
        assert(args[0] == "--help")
        if (args.size != 1) {
            ErrorCommand.print("To get help, run as follows: '--help'.")
            return user
        }
        print("***** Program usage ******")
        print(ExitCommand.usage)
        print(RegisterUserCommand.usage)
        print(LoginUserCommand.usage)
        print(LogOutUserCommand.usage)
        print(GetItemsCommand.usage)
        print(GetGroupsCommand.usage)
        print(GetLabelsCommand.usage)
        print("**************************")
        println()
        return user
    }
}
