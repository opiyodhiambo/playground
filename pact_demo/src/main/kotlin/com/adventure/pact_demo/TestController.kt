package com.adventure.pact_demo

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class TestController {

    @PostMapping("/newNumber")
    fun newNumber(@RequestBody nextNumberRequest: NewNumberRequest): FinalNumberResponse {
        val operationId = UUID.randomUUID()
        val totalNumber = nextNumberRequest.currentNumber * nextNumberRequest.factor
        return FinalNumberResponse(operationId, totalNumber)
    }
}

data class NewNumberRequest(
    val currentNumber: Int,
    val factor: Int
)

data class FinalNumberResponse(
    val operationId: UUID,
    val totalNumber: Int
)