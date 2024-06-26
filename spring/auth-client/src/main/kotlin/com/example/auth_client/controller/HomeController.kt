package com.example.auth_client.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class HomeController {

    @GetMapping
    fun home(): String {
        return "index.html"
    }
}