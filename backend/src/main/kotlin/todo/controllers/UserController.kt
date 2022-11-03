package todo.controllers

import models.User
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import todo.service.Service

@RestController
internal class UserController(private val service: Service) {

    @PostMapping("/user")
    fun login(@RequestBody user: User) {
        try {
            service.logInUser(user)
        } catch (illegalArgumentException: IllegalArgumentException) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find user with username ${user.username} and password ${user.password}", illegalArgumentException)
        }
    }

    @DeleteMapping("/user/{username}")
    fun deleteUser(@PathVariable username: String?) {
        service.authenticate()
        username?.let { service.removeUser(username) }
    }

    @PostMapping("/register")
    fun register(@RequestBody user: User) {
        try {
            service.registerUser(user)
        } catch (illegalArgumentException: IllegalArgumentException) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find user with username ${user.username} and password ${user.password}", illegalArgumentException)
        }
    }
}
