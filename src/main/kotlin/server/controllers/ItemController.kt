package server.controllers

import models.Item
import server.Service

import org.springframework.web.bind.annotation.*

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