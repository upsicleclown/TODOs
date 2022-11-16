package commands

import models.User

/**
 * Command interface for the command pattern. All valid commands should implement this interface.
 */
interface Command {
    fun execute(user: User?): User?
}
