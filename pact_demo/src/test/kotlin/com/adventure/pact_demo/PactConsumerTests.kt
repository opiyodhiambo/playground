package com.adventure.pact_demo

import au.com.dius.pact.consumer.dsl.PactDslWithProvider
import au.com.dius.pact.consumer.junit5.PactConsumerTest
import au.com.dius.pact.consumer.junit5.PactTestFor
import au.com.dius.pact.core.model.RequestResponsePact
import au.com.dius.pact.core.model.annotations.Pact
import au.com.dius.pact.provider.junitsupport.loader.PactBroker

@PactConsumerTest
@PactTestFor
@PactBroker(host = "localhost", port = "9292")
class PactConsumerTests {

    @Pact(provider = "TestProvider", consumer = "TestConsumer")
    fun createPact(builder: PactDslWithProvider): RequestResponsePact {
        return builder
            .uponReceiving("PactProviderTests test interaction")
            .path("/newNumber")
            .method("POST")
            .willRespondWith()
            .status(200)
            .toPact()
    }
}
