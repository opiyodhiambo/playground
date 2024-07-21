package io.tajji.auth_server_dummy


import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint

import java.util.UUID

@Configuration
class AppConfig(private val roleBasedSuccessHandler: CustomAuthenticationSuccessHandler) {

    @Bean
    @Order(1)
    fun asFilterChain(http: HttpSecurity): SecurityFilterChain {
        OAuth2AuthorizationServerConfiguration
            .applyDefaultSecurity(http)

        http.getConfigurer(OAuth2AuthorizationServerConfigurer::class.java)
            .oidc(Customizer.withDefaults())
            .clientAuthentication { it.authenticationSuccessHandler(roleBasedSuccessHandler) }

        http.exceptionHandling {
            it.authenticationEntryPoint(
                LoginUrlAuthenticationEntryPoint("https://4516-41-90-124-47.ngrok-free.app/login")
            )

        }
        return http.build()

    }

    @Bean
    @Order(2)
    fun defaultSecurityFilterChain(http: HttpSecurity): SecurityFilterChain {

        return http.formLogin{it.loginPage("https://4516-41-90-124-47.ngrok-free.app/login")}
            .logout(Customizer.withDefaults())
            .authorizeHttpRequests {
                it.requestMatchers("/error").permitAll()
                it.anyRequest().authenticated()
            }
            .build()
    }

    @Bean
    fun jwtCustomizer(): OAuth2TokenCustomizer<JwtEncodingContext> =
        OAuth2TokenCustomizer {
            val portfolioDetails = listOf(
                PortfolioDetails(
                    portfolioName = "Mugo's Portfolio",
                    portfolioImageURL = ""
                ),
                PortfolioDetails (
                    portfolioName = "Kahiga's Portfolio",
                    portfolioImageURL = ""
                ),
                PortfolioDetails(
                    portfolioName = "Kambi's Portfolio",
                    portfolioImageURL = "https://tajji.io/logo.png"
                ),
                PortfolioDetails(
                    portfolioName = "Opiyo's Portfolio",
                    portfolioImageURL = "https://tajji-landing-page-media.ams3.cdn.digitaloceanspaces.com/landing-page/bomasection.webp"
                )
            )
            val portfolioMemberships = portfolioDetails.map { detail ->
                createPortfolioClaim(portfolioName = detail.portfolioName, portfolioImageURL = detail.portfolioImageURL)
            }.toSet()
            it.claims.claim("portfolio_memberships", portfolioMemberships)
        }

    private fun createPortfolioClaim(portfolioName: String, portfolioImageURL: String): PortfolioClaim{
        val portfolioId = UUID.randomUUID()
        val propertyId = UUID.randomUUID()
        val portfolioUrlSlug = portfolioName
            .replace("'", " ")
            .split(" ")[0]
            .lowercase()
        return PortfolioClaim(
            portfolioId = portfolioId,
            portfolioName = portfolioName,
            portfolioURL = "https://$portfolioUrlSlug.tajji.io/api",
            portfolioImageURL = portfolioImageURL,
            billingStatus = PortfolioBillingStatus.STABLE,
            portfolioRoles = setOf(
                PortfolioRole(
                    portfolioId = portfolioId,
                    roles = setOf(
                        AccountRole.ADMIN,
                        AccountRole.LANDLORD
                    )
                )
            ),
            propertyRoles = setOf(
                PropertyRole(
                    propertyId = propertyId,
                    roles = setOf(
                        AccountRole.RESIDENT
                    )
                )
            )
        )

    }
}

data class PortfolioDetails(
    val portfolioName: String,
    val portfolioImageURL: String
)

