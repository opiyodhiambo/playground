package com.adventure.order_service.service

import com.adventure.order_service.dao.Order
import com.adventure.order_service.dao.OrderRepository
import com.adventure.order_service.datamodel.OrderStatus
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class OrderService(private val orderRepository: OrderRepository) {

    fun getAllOrders(): Flux<Order> {
        return orderRepository.findAll()
    }

    fun submitOrder(isbn: String, quantity: Int): Mono<Order> {
        return Mono.just(buildRejectedOrder(isbn = isbn, quantity = quantity))
            .flatMap(orderRepository::save)
    }

    private fun buildRejectedOrder(isbn: String, quantity: Int): Order {
        return Order.of(isbn, null, null, quantity, OrderStatus.REJECTED)
    }
}