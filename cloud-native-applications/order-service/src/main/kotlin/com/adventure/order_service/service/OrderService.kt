package com.adventure.order_service.service

import com.adventure.order_service.dto.Order
import com.adventure.order_service.dto.OrderRepository
import com.adventure.order_service.datamodel.OrderStatus
import com.adventure.order_service.dto.Events
import kotlinx.coroutines.reactor.flux
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class OrderService(
    private val orderRepository: OrderRepository
) {

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

    fun consumeOrderDispatchedEvent(flux: Flux<Events.OrderDispatchedMessage>): Flux<Order> {
        return flux {
            flux.flatMap { orderRepository.findById(it.orderId) }
                .map { buildDispatchedOrder(it) }
                .flatMap { orderRepository.save(it) }
        }
    }

    private fun buildDispatchedOrder(existingOrder: Order): Order {
        return Order(
            id = existingOrder.id,
            bookIsbn = existingOrder.bookIsbn,
            bookName = existingOrder.bookName,
            bookPrice = existingOrder.bookPrice,
            quantity = existingOrder.quantity,
            status = OrderStatus.DISPATCHED,
            createdDate = existingOrder.createdDate,
            createdBy = existingOrder.createdBy,
            lastModifiedDate = existingOrder.lastModifiedDate,
            lastModifiedBy = existingOrder.lastModifiedBy,
            version = existingOrder.version
        )
    }
}