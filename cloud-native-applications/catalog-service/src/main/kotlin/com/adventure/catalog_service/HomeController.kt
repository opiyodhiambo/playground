package com.adventure.catalog_service

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HomeController(private val properties: PolarProperties) {

    @GetMapping("/")
    fun getGreeting(): String {
        return properties.getGreeting()
    }
}