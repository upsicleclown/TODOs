package server.controllers

import models.Label
import server.Service

import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController


@RestController
internal class LabelController(private val service: Service) {

    @GetMapping("/labels")
    fun all(): List<Label> {
        return service.getLabels()
    }

    @PostMapping("/labels")
    fun newLabel(@RequestBody newLabel: Label?) {
        newLabel?.let { service.addLabel(it) }
    }

    @DeleteMapping("/labels/{id}")
    fun deleteLabel(@PathVariable id: Int?) {
        id?.let { service.removeLabel(it) }
    }

}