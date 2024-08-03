package com.adventure.auth_server

import com.adventure.auth_server.api.AccountCreationController.LoginRequest
import com.adventure.auth_server.api.AccountCreationController.SubmitCredentialsRequest
import com.adventure.auth_server.auth.components.CustomUserDetails
import com.adventure.auth_server.auth.components.CustomUserDetailsService
import com.adventure.auth_server.auth.config.AccountStatus
import com.adventure.auth_server.auth.config.RoleBasedAuthenticationHandler
import com.adventure.auth_server.auth.persistence.AccountRole
import com.adventure.auth_server.auth.persistence.UsersEntity
import com.adventure.auth_server.auth.persistence.UsersRepository
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.*
import kotlin.test.assertEquals

@SpringBootTest
@AutoConfigureMockMvc
class AuthServerApplicationTests {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var roleBasedAuthenticationHandle: RoleBasedAuthenticationHandler

    @Autowired
    private lateinit var usersRepository: UsersRepository

    @MockBean
    private lateinit var customUserDetailsService: CustomUserDetailsService

    @Test
    fun `should create account and redirect with JWT token`() {

        val request = SubmitCredentialsRequest(
            emailAddress = "test@tajji.io",
            password = "password",
            termsOfUse = true
        )

        mockMvc.perform(
            MockMvcRequestBuilders.post("/onboarding/submitCredentials")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ObjectMapper().writeValueAsString(request))
        )
            .andExpect(status().isOk)
            .andExpect(MockMvcResultMatchers.content().string("Karibu Tajji"))
            .andReturn()
    }

    @Test
    fun `should redirect to success URL if account status is active`() {
        val request = MockHttpServletRequest()
        val response = MockHttpServletResponse()
        val authentication = Mockito.mock(Authentication::class.java)
        val usersEntity = UsersEntity.newUser(
            principalId = UUID.randomUUID(),
            userName = "user@tajji.io",
            password = "password",
            status = AccountStatus.EMAIL_VERIFICATION_PENDING,
            role = AccountRole.LANDLORD
        )
        val userDetails = CustomUserDetails(usersEntity = usersEntity)
        Mockito.`when`(authentication.details).thenReturn(userDetails)
        roleBasedAuthenticationHandle.onAuthenticationSuccess(
            request, response, authentication
        )
        assertEquals(
            response.redirectedUrl.toString(),
            "https://1d1dc82944ab93060cbb7abd4449a0b0.serveo.net/platformOnboarding"
        )
    }

    @Test
    fun `should return Ok for authenticated user`() {

        val request = LoginRequest(
            username = "example@example.com",
            password = "yourPassword"
        )

        // Create a mock UsersEntity
        val usersEntity = UsersEntity.newUser(
            principalId = UUID.randomUUID(),
            userName = "example@example.com",
            password = "yourPassword",
            role = AccountRole.LANDLORD,
            status = AccountStatus.EMAIL_VERIFICATION_PENDING
        )
        usersRepository.save(usersEntity)
        Thread.sleep(3000)

        // Perform the login test
        mockMvc.perform(
            MockMvcRequestBuilders.post("/authenticateUser")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ObjectMapper().writeValueAsString(request))
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().string("yes"))
            .andReturn()
    }

    @Test
    fun `should redirect to customLoginPage on unauthenticated user`() {

        val request = LoginRequest(
            username = "example@example.com",
            password = "yourPassword"
        )

        // Create a mock UsersEntity
        val usersEntity = UsersEntity.newUser(
            principalId = UUID.randomUUID(),
            userName = "example@example.com",
            password = "yourPassword",
            role = AccountRole.LANDLORD,
            status = AccountStatus.EMAIL_VERIFICATION_PENDING
        )
        usersRepository.save(usersEntity)

        // Perform the login test
        mockMvc.perform(
            MockMvcRequestBuilders.post("/authenticateUser")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ObjectMapper().writeValueAsString(request))
        )
            .andExpect(MockMvcResultMatchers.status().is3xxRedirection)
            .andExpect(MockMvcResultMatchers.header().string("Location", "tajji.io"))
            .andReturn()
    }
}

data class LoginRequest(
    val userName: String,
    val password: String
)
