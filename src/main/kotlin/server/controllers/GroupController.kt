package server.controllers

import models.Group
import server.Service

import org.springframework.web.bind.annotation.*


@RestController
internal class GroupController(private val service: Service) {

    @GetMapping("/groups")
    fun all(): List<Group> {
        return service.getGroups()
    }

    @PostMapping("/groups")
    fun newGroup(@RequestBody newGroup: Group?) {
        newGroup?.let { service.addGroup(it) }
    }

    @DeleteMapping("/groups/{id}")
    fun deleteGroup(@PathVariable id: Int?) {
        id?.let { service.removeGroup(it) }
    }

    @PutMapping("/groups/{id}")
    fun editGroup(@RequestBody newGroup: Group?, @PathVariable id: Int?) {
        newGroup?.let {
            id?.let {
                service.editGroup(id, newGroup)
            }
        }
    }
}