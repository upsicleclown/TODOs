package todo.controllers

import models.Label
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import todo.service.Service
import todo.service.authentication.AuthenticationService

@RestController
internal class LabelController(private val service: Service, private val authenticationService: AuthenticationService) {

    @GetMapping("/labels")
    fun all(): List<Label> {
        authenticationService.authenticate()
        return service.getLabels()
    }

    @PostMapping("/labels")
    fun newLabel(@RequestBody newLabel: Label?) {
        authenticationService.authenticate()
        newLabel?.let { service.addLabel(it) }
    }

    @DeleteMapping("/labels/{id}")
    fun deleteLabel(@PathVariable id: Int?) {
        authenticationService.authenticate()
        id?.let { service.removeLabel(it) }
    }
}
