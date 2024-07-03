package io.tajji.auth_server_dummy
//
//import org.hamcrest.Matchers.containsString
//import org.hamcrest.Matchers.`is`
//import org.junit.jupiter.api.Test
//import org.slf4j.Logger
//import org.slf4j.LoggerFactory
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
//import org.springframework.boot.test.context.SpringBootTest
//import org.springframework.mock.web.MockHttpSession
//import org.springframework.security.oauth2.client.registration.ClientRegistration
//import org.springframework.security.oauth2.core.AuthorizationGrantType
//import org.springframework.security.oauth2.core.ClientAuthenticationMethod
//import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest
//import org.springframework.security.oauth2.core.oidc.OidcScopes
//import org.springframework.security.oauth2.core.user.OAuth2User
//import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository
//import org.springframework.security.oauth2.server.authorization.client.RegisteredClient
//import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository
//import org.springframework.security.test.context.support.WithMockUser
//import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin
//import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*
//import org.springframework.test.web.servlet.MockMvc
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
//import org.springframework.web.util.UriComponentsBuilder
//import java.util.UUID
//import kotlin.test.assertNotNull
//
//@SpringBootTest
//@AutoConfigureMockMvc
//class AuthServerDummyApplicationTests {
//
////    @Autowired
////    private lateinit var mvc: MockMvc
////
////    @Autowired private lateinit var repo: InMemoryRegisteredClientRepository
////
////    @Autowired
////    private lateinit var clientRepository: RegisteredClientRepository
////    val logger: Logger = LoggerFactory.getLogger(this::class.java)
////
////    @Test
////    fun unauthenticatedUser() {
////        mvc.perform(get("/hello"))
////            .andExpect(status().is3xxRedirection)
////            .andExpect(redirectedUrlPattern("**/login"))
////
////    }
////
////    @Test
////    @WithMockUser
////    fun authenticatedMockUser() {
////        mvc.perform(get("/hello"))
////            .andExpect(status().isOk)
////            .andExpect(content().string(containsString("Hello too")))
////    }
////
////
////    @Test
////    fun authenticatedUser() {
////        val cli = clientRepository.findByClientId("tajjiboma")!!
////
////        val client: ClientRegistration = ClientRegistration.withRegistrationId(cli.id)
////            .clientId(cli.clientId)
////            .clientSecret(cli.clientSecret)
////            .clientAuthenticationMethod(cli.clientAuthenticationMethods.first())
////            .authorizationGrantType(cli.authorizationGrantTypes.first())
////            .tokenUri("http://localhost:8080/oauth2/token")
////            .redirectUri(cli.redirectUris.first())
////            .scope(cli.scopes.first())
////            .build()
////
////        mvc.perform(
////            get("/hello")
////                .with(oauth2Client().clientRegistration(client)))
////            .andExpect (status().isOk)
////    }
////
////    @Test
////    fun clientCredentialsGrantType() {
////        val httpSession = MockHttpSession()
////        val clientRegistration = clientRepository.findByClientId("tajjiboma")
////
////        requireNotNull(clientRegistration) { "Client registration not found" }
////        val authorizationRequest = OAuth2AuthorizationRequest.authorizationCode()
////            .clientId(clientRegistration.clientId)
////            .redirectUri(clientRegistration.redirectUris.first())
////            .authorizationUri(clientRegistration.authorizationGrantTypes.first { it == AuthorizationGrantType.AUTHORIZATION_CODE }
////                .toString())
////            .scopes(clientRegistration.scopes)
////            .build()
////
////        logger.info("AuthorizationRequest :: ${authorizationRequest.redirectUri}")
////
////        httpSession.setAttribute("authRequest", authorizationRequest)
////
////        val result = mvc
////            .perform(
////                get(clientRegistration.redirectUris.first())
////                    .session(httpSession)
////                    .param("code", UUID.randomUUID().toString())
////                    .param("state", "some-state")
////            )
////
////        result.andExpect(status().is3xxRedirection)
////        logger.info("result :: $result")
////    }
//
//    @Test
//    fun contextLoads() {
//    }
//}
