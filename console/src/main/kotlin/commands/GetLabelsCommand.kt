package commands

import client.TODOClient
import models.Label
import java.io.IOException

class GetLabelsCommand(private val args: List<String>) : Command {

    companion object {
        const val usage = "To list all labels, run as follows: '--labels'."
    }

    override fun execute() {
        assert(args[0] == "--labels")
        if (args.size != 1) {
            throw IllegalArgumentException(usage)
        }
        try {
            val labels: List<Label> = TODOClient().getLabels()
            println("***** Labels in database *****")
            for (label in labels) {
                println(label)
            }
            println()
        } catch (ignore: IOException) {
            ErrorCommand.print("You must first login to list groups.")
        }
    }
}
