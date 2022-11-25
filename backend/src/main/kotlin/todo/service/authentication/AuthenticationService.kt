package todo.service.authentication

import models.User
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import todo.database.SQLiteDB

@Service
class AuthenticationService {
    private val authenticationHelper = AuthenticationHelper()
    private var isUserLoggedIn = false
    private var userLoggedIn: User? = null

    //
    // User endpoints.
    //

    /**
     * Ensures user is authenticated.
     *
     * @throws IllegalArgumentException
     */
    fun authenticate() {
        if (!isUserLoggedIn) {
            throw ResponseStatusException(HttpStatus.FORBIDDEN, "User not authenticated")
        }
    }

    /**
     * Logs in a user.
     *
     * @throws IllegalArgumentException if username does not exist or password does not match.
     */
    fun logInUser(user: User): User {
        userLoggedIn = SQLiteDB.getUser(user, authenticationHelper.computePasswordHash(user.password))
        isUserLoggedIn = true
        return SQLiteDB.setUserLoggedIn(userLoggedIn!!)
    }

    /**
     * Logs out user currently logged in if any.
     */
    fun logOutUser() {
        isUserLoggedIn = false
        SQLiteDB.resetUserLoggedIn()
    }

    /**
     * Registers a user and returns user with their newly generated token.
     *
     * @throws IllegalArgumentException if username already exists or password is empty.
     */
    fun registerUser(user: User) {
        if (user.password.isEmpty() || user.password.isBlank()) {
            throw IllegalArgumentException("Password for user ${user.username} cannot be empty.")
        }
        SQLiteDB.addUser(user, authenticationHelper.computePasswordHash(user.password))
    }

    /**
     * Deletes the user with the provided username.
     *
     * @throws IllegalArgumentException if no such user with provided id.
     */
    fun removeUser(username: String) {
        SQLiteDB.removeUser(username)
    }
}
