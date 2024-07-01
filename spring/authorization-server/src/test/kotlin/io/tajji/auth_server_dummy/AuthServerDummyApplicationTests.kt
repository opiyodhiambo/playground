package io.tajji.auth_server_dummy

import org.hamcrest.Matchers.containsString
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@SpringBootTest
@AutoConfigureMockMvc
class AuthServerDummyApplicationTests {

    @Autowired
    private lateinit var mvc: MockMvc

    @Test
    fun unauthenticatedUser() {
        mvc.perform(get("/hello"))
            .andExpect(status().is3xxRedirection)
            .andExpect(redirectedUrlPattern("**/login"))

    }

    @Test
    @WithMockUser
    fun authenticatedMockUser() {
        mvc.perform(get("/hello"))
            .andExpect(status().isOk)
            .andExpect(content().string(containsString("Hello too")))
    }

    @Test
    fun authenticatedUser() {
        mvc.perform(formLogin().user("Mugo").password("RiekoIoane@13"))
            .andExpect(status().is3xxRedirection)
            .andExpect(redirectedUrl("https://tajji.io/"))
    }
}
