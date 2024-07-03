package com.adventure.catalog_service

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HomeController {

    @GetMapping("/")
    fun getGreeting(): String {
        return "Welcome to the book catalog"
    }
}