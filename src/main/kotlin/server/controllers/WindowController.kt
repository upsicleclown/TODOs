package server.controllers

import models.WindowSettings
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import server.Service

@RestController
internal class WindowController(private val service: Service) {

    @GetMapping("/window-settings")
    fun all(): WindowSettings {
        return service.getWindowSettings()
    }

    @PutMapping("/window-settings")
    fun editItem(@RequestBody newWindowSettings: WindowSettings?) {
        newWindowSettings?.let {
            service.editWindowSettings(newWindowSettings)
        }
    }
}
