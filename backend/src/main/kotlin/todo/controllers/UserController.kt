package todo.controllers

import models.User
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import todo.service.authentication.AuthenticationService

@RestController
internal class UserController(private val authenticationService: AuthenticationService) {

    @PostMapping("/user")
    fun login(@RequestBody user: User): User {
        try {
            return authenticationService.logInUser(user)
        } catch (illegalArgumentException: IllegalArgumentException) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find user with username ${user.username} and password ${user.password}", illegalArgumentException)
        }
    }

    @PostMapping("/logout")
    fun logout() {
        authenticationService.logOutUser()
    }

    @DeleteMapping("/user/{username}")
    fun deleteUser(@PathVariable username: String?) {
        authenticationService.authenticate()
        username?.let { authenticationService.removeUser(username) }
    }

    @PostMapping("/register")
    fun register(@RequestBody user: User) {
        try {
            authenticationService.registerUser(user)
        } catch (illegalArgumentException: IllegalArgumentException) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Could not register user with username ${user.username} and password ${user.password}", illegalArgumentException)
        }
    }
}
