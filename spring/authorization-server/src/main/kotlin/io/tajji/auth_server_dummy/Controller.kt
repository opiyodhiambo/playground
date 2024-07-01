package io.tajji.auth_server_dummy

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class Controller {

    @GetMapping("/hello")
    fun hello(): String {
        return "Hello too"
    }
}