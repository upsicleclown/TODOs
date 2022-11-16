import commands.CommandFactory
import commands.HelpCommand
import models.User

fun main() {
    HelpCommand(listOf("--help")).execute(null)
    var loggedInUser: User? = null
    while (true) {
        val args: List<String> = readln().split(" ")
        val command = CommandFactory.createFromArgs(args)
        loggedInUser = command.execute(loggedInUser)
    }
}
