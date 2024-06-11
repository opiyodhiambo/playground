package com.adventure.demo.api

import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID


@RestController
class TestController(
    private val testService: TestService
) {
    @GetMapping("/hello")
    @CrossOrigin("http://localhost:8081") // Allows the port 8081 of localhost origin for cross-origin requests
    fun getHello(): ResponseEntity<String> {
        return ResponseEntity.ok("Get Hello!")
    }

    @PostMapping("/hello")
    fun postHello(): String {
        return "Post Hello!"
    }

    @GetMapping("/lastName")
    @PreAuthorize("hasAuthority('WRITE')")
    fun getLastName(): String {
        return "Opiyo"
    }

    @PostMapping("/sell")
    fun sellProduct(productName: MutableList<Product>) {
        testService.sellProduct(name = productName)
    }
}

data class Product(
    val productName: String,
    val productId: UUID
)