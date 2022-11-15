package commands

/**
 * Factory pattern.
 *
 * Given a list of arguments read from the command-line, this static object will return the appropriate command.
 */
object CommandFactory {
    fun createFromArgs(args: List<String>): Command {
        if (args.isEmpty()) {
            return HelpCommand(args)
        }
        return when (args[0]) {
            "--help" -> HelpCommand(args)
            "--exit" -> ExitCommand(args)
            "--register" -> RegisterUserCommand(args)
            "--login" -> LoginUserCommand(args)
            "--logout" -> LogOutUserCommand(args)
            "--items" -> GetItemsCommand(args)
            "--groups" -> GetGroupsCommand(args)
            "--labels" -> GetLabelsCommand(args)
            else -> ErrorCommand(args)
        }
    }
}
