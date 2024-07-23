package com.adventure.disaptcher_service

import com.adventure.disaptcher_service.dto.OrderAcceptedMessage
import com.adventure.disaptcher_service.dto.OrderDispatchedMessage
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cloud.function.context.FunctionCatalog
import org.springframework.cglib.core.internal.Function
import org.springframework.cloud.function.context.test.FunctionalSpringBootTest
import reactor.core.publisher.Flux
import reactor.test.StepVerifier

@FunctionalSpringBootTest
class DispatchingFunctionsIntegrationTests {

    @Autowired
    private lateinit var catalog: FunctionCatalog

    @Test
    fun packAndLabelOrder() {
        val packAndLabel = catalog
            .lookup(Function::class.java, "pack|label") as Function<OrderAcceptedMessage, Flux<OrderDispatchedMessage>>
        val orderId: Long = 121
        StepVerifier.create(packAndLabel.apply(OrderAcceptedMessage(orderId = orderId)))
            .expectNextMatches { it == OrderDispatchedMessage(orderId = orderId) }
            .verifyComplete()
    }

    @Test
    fun contextLoads() {
    }

}
