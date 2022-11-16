package commands

import client.TODOClient
import models.Label
import models.User
import java.io.IOException

class GetLabelsCommand(private val args: List<String>) : Command {

    companion object {
        const val usage = "To see the list of labels, run as follows: '--labels'."
    }

    override fun execute(user: User?): User? {
        assert(args[0] == "--labels")
        if (args.size != 1) {
            ErrorCommand.print(usage)
            return user
        }
        try {
            val labels: List<Label> = TODOClient().getLabels()
            println("***** [${user!!.username}] Labels in database *****")
            for (label in labels) {
                println(label)
            }
            println()
        } catch (ignore: IOException) {
            ErrorCommand.print("You must first login to see the list of labels.")
        }
        return user
    }
}
