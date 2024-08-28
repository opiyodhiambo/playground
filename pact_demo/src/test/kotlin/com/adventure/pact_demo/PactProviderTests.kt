package com.adventure.pact_demo

import au.com.dius.pact.provider.junit5.PactVerificationContext
import au.com.dius.pact.provider.junit5.PactVerificationInvocationContextProvider
import au.com.dius.pact.provider.junitsupport.Provider
import au.com.dius.pact.provider.junitsupport.loader.PactBroker
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestTemplate
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
@Provider("myAwesomeService")
@PactBroker
class PactProviderTests {

	@Test
	fun contextLoads() {
	}

	@TestTemplate
	@ExtendWith(PactVerificationInvocationContextProvider::class)
    fun pactVerificationTestTemplate(pactVerificationContext: PactVerificationContext){
        pactVerificationContext.verifyInteraction()
    }

}
