package com.adventure.onboarding_svc

import com.adventure.onboarding_svc.api.AccountStatus
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.web.client.RestTemplate
import org.springframework.web.reactive.function.client.WebClient

//@SpringBootTest
//@AutoConfigureMockMvc
//class OnboardingSvcApplicationTests {
//
//    @Autowired
//    private lateinit var mockMvc: MockMvc
//
//    private val webClient = WebClient.builder()
//        .baseUrl("http://localhost:8000")
//        .build()
//
//    @Test
//    fun `should return EMAIL_VERIFICATION_PENDING with valid JWT token`() {
//
//        val token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0QHRhamppLmlvIiwiaWF0IjoxNzIxODQ3NTQ0LCJleHAiOjE3MjE4NTExNDR9.uaSIRDS91XpfU24pxloPBUFoGimnqA7OmqHMab3Z9V8"
//
//        mockMvc.perform(
//            MockMvcRequestBuilders.get("/onboarding/getAccountStatus")
//                .contentType(MediaType.APPLICATION_JSON)
//                .header(HttpHeaders.AUTHORIZATION, "Bearer $token")
//        )
//            .andExpect(MockMvcResultMatchers.status().isOk)
//            .andExpect(MockMvcResultMatchers.content().string(AccountStatus.EMAIL_VERIFICATION_PENDING.name))
//    }
//
//    private fun getAccessToken(): String {
//
//        val response = webClient.post()
//            .uri("/onboarding/submitCredentials")
//            .contentType(MediaType.APPLICATION_JSON)
//            .bodyValue(
//                SubmitCredentialsRequest(
//                    emailAddress = "test@tajji.io",
//                    password = "password",
//                    termsOfUse = true
//                )
//            )
//            .retrieve()
//            .toBodilessEntity()
//            .block()
//
//        if (response != null && response.statusCode == HttpStatus.FOUND) {
//            val redirectUrl = response.headers.location.toString()
//            val tokenParameter = "token="
//            return if (redirectUrl.contains(tokenParameter)) {
//                redirectUrl.substringAfter(tokenParameter).substringBefore("&")
//            } else {
//                throw IllegalStateException("Token not found in redirect URL")
//            }
//        } else {
//            throw IllegalStateException("Failed to retrieve redirect URL or response is not FOUND")
//        }
//    }
//}
//
//data class SubmitCredentialsRequest(
//    val emailAddress: String,
//    val password: String,
//    val termsOfUse: Boolean
//)
