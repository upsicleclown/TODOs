package commands

import kotlin.system.exitProcess

class ExitCommand(private val args: List<String>) : Command {

    companion object {
        const val usage = "To exit, run as follows: '--exit'."
    }

    override fun execute() {
        assert(args[0] == "--exit")
        if (args.size != 1) {
            throw IllegalArgumentException(usage)
        }
        exitProcess(0)
    }
}
