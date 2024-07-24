package com.adventure.order_service.controllers

import com.adventure.order_service.dto.Order
import com.adventure.order_service.datamodel.OrderRequest
import com.adventure.order_service.service.OrderService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
class OrderController(private val orderService: OrderService) {

    @GetMapping
    fun getAllOrders(): ResponseEntity<Flux<Order>> =
        ResponseEntity.ok(orderService.getAllOrders())

    @PostMapping
    fun submitOrder(@RequestBody @Valid request: OrderRequest): ResponseEntity<Mono<Order>> =
        ResponseEntity.ok(orderService.submitOrder(isbn = request.isbn, quantity = request.quantity))
}