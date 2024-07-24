package com.adventure.order_service.configuration

import com.adventure.order_service.dto.Events
import com.adventure.order_service.service.OrderService
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import reactor.core.publisher.Flux
import java.util.function.Consumer

@Configuration
class OrderFunctions {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @Bean
    fun dispatchOrder(orderService: OrderService): Consumer<Flux<Events.OrderDispatchedMessage>> {
        return Consumer {
            orderService.consumeOrderDispatchedEvent(it)
                .doOnNext { order -> logger.info("The order with it ${order.id} is dispatched") }
                .subscribe()
        }
    }
}