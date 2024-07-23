package io.tajji.auth_server_dummy

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URI

//@RestController
//class Controller {
//
//    @GetMapping("/hello")
//    fun hello(): String {
//        return "Hello too"
//    }
//}

@RestController
class UserManagementController {

    @PostMapping("/loginSuccess")
    fun loginSuccess(): ResponseEntity<Void> {
        val headers = HttpHeaders()
        headers.location = URI.create("https://tajji.io")
        return ResponseEntity<Void>(headers, HttpStatus.MOVED_PERMANENTLY)
    }

    @PostMapping("/loginFailure")
    fun loginError(): ResponseEntity<Void> {
        val headers = HttpHeaders()
        headers.location = URI.create("https://tajji.io")
        return ResponseEntity<Void>(headers, HttpStatus.MOVED_PERMANENTLY)
    }
}