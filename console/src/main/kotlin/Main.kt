import commands.CommandFactory
import commands.HelpCommand

fun main() {
    HelpCommand(listOf("--help")).execute()
    while (true) {
        val args: List<String> = readln().split(" ")
        val command = CommandFactory.createFromArgs(args)
        command.execute()
    }
}
