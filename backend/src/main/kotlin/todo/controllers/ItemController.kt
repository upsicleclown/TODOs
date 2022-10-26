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

@RestController
internal class ItemController(private val service: Service) {

    @GetMapping("/items")
    fun all(): List<Item> {
        return service.getItems()
    }

    @PostMapping("/items")
    fun newItem(@RequestBody newItem: Item?) {
        newItem?.let { service.addItem(it) }
    }

    @DeleteMapping("/items/{id}")
    fun deleteItem(@PathVariable id: Int?) {
        id?.let { service.removeItem(it) }
    }

    @PutMapping("/items/{id}")
    fun editItem(@RequestBody newItem: Item?, @PathVariable id: Int?) {
        newItem?.let {
            id?.let {
                service.editItem(id, newItem)
            }
        }
    }
}
