package todo.controllers

import models.Group
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import todo.service.Service
import todo.service.authentication.AuthenticationService

@RestController
internal class GroupController(private val service: Service, private val authenticationService: AuthenticationService) {

    @GetMapping("/groups")
    fun all(): List<Group> {
        authenticationService.authenticate()
        return service.getGroups()
    }

    @PostMapping("/groups")
    fun newGroup(@RequestBody newGroup: Group?): Group? {
        authenticationService.authenticate()
        if (newGroup == null) {
            return null
        }
        return service.addGroup(newGroup)
    }

    @DeleteMapping("/groups/{id}")
    fun deleteGroup(@PathVariable id: Int?) {
        authenticationService.authenticate()
        id?.let { service.removeGroup(it) }
    }

    @PutMapping("/groups/{id}")
    fun editGroup(@RequestBody newGroup: Group?, @PathVariable id: Int?) {
        authenticationService.authenticate()
        newGroup?.let {
            id?.let {
                service.editGroup(id, newGroup)
            }
        }
    }
}
