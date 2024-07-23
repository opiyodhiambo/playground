package com.adventure.disaptcher_service

import com.adventure.disaptcher_service.dto.OrderAcceptedMessage
import com.adventure.disaptcher_service.dto.OrderDispatchedMessage
import org.slf4j.LoggerFactory
import org.springframework.cglib.core.internal.Function
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import reactor.core.publisher.Flux

@Configuration
class DispatchingFunctions {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @Bean
    fun pack(): Function<OrderAcceptedMessage, Long> {
        return Function {
            logger.info("The order with id ${it.orderId} is packed")
            it.orderId
        }
    }

    @Bean
    fun label(): Function<Flux<Long>, Flux<OrderDispatchedMessage>> {
        return Function { order ->
            logger.info("The order with id $order is labelled")
            order.map {
                OrderDispatchedMessage(it)
            }

        }
    }
}