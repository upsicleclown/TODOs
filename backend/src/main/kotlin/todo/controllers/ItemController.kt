package todo.controllers

import models.Item
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
internal class ItemController(private val service: Service, private val authenticationService: AuthenticationService) {

    @GetMapping("/items")
    fun all(): List<Item> {
        authenticationService.authenticate()
        return service.getItems()
    }

    @PostMapping("/items")
    fun newItem(@RequestBody newItem: Item?): Item? {
        authenticationService.authenticate()
        if (newItem == null) {
            return null
        }
        return service.addItem(newItem)
    }

    @DeleteMapping("/items/{id}")
    fun deleteItem(@PathVariable id: Int?) {
        authenticationService.authenticate()
        id?.let { service.removeItem(it) }
    }

    @PutMapping("/items/{id}")
    fun editItem(@RequestBody newItem: Item?, @PathVariable id: Int?) {
        authenticationService.authenticate()
        newItem?.let {
            id?.let {
                service.editItem(id, newItem)
            }
        }
    }
}
