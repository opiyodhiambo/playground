package io.tajji.auth_server_dummy


//import io.tajji.auth_server_dummy.OperationalStatus.OPERATIONAL
import io.tajji.auth_server_dummy.AccountRole.OperationalStatus.OPERATIONAL
import io.tajji.auth_server_dummy.PortfolioBillingStatus.STABLE
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.core.Authentication
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
    @Order(value = 1)
    fun asFilterChain(http: HttpSecurity): SecurityFilterChain {
        OAuth2AuthorizationServerConfiguration
            .applyDefaultSecurity(http)

        http.getConfigurer(OAuth2AuthorizationServerConfigurer::class.java)
            .oidc(Customizer.withDefaults())

        http.exceptionHandling {
            it.authenticationEntryPoint(
                LoginUrlAuthenticationEntryPoint("/login")
            )
        }

        return http.build()

    }

    @Bean
    @Order(value = 2)
    fun defaultSecurityFilterChain(http: HttpSecurity): SecurityFilterChain {

        http.formLogin(Customizer.withDefaults())

        http.authorizeHttpRequests {
            it.anyRequest().authenticated()
        }

        return http.build()

    }

    @Bean
    fun jwtCustomizer(): OAuth2TokenCustomizer<JwtEncodingContext> {
        return OAuth2TokenCustomizer { context: JwtEncodingContext ->
            val user = context.getPrincipal() as Authentication
            val username = user.name

//            val memberships = accounts.retrievePortfolioMemberships(
//                username = username, pagination = Pageable.unpaged())
//                .getOrElse { throw it }
            val memberships = setOf(
                PortfolioClaim(
                    portfolioId = UUID.randomUUID(),
                    operationalStatus = OPERATIONAL,
                    portfolioName = "Mugo Portfolio",
                    portfolioImageURL = "",
                    portfolioURL = "https://mugo.tajji.io",
                    billingStatus = STABLE,
                    portfolioRoles = setOf(
                        AccountRole.LANDLORD
                    ),
                    propertyRoles = setOf(
                        PropertyRole(
                            propertyId = UUID.randomUUID(),
                            roles = AccountRole.LANDLORD
                        )
                    )
                ),
                PortfolioClaim(
                    portfolioId = UUID.randomUUID(),
                    operationalStatus = OPERATIONAL,
                    portfolioName = "Opiyo Portfolio",
                    portfolioImageURL = "https://tajji-landing-page-media.ams3.cdn.digitaloceanspaces.com/landing-page/bomasection.webp",
                    portfolioURL = "https://opiyo.tajji.io",
                    billingStatus = STABLE,
                    portfolioRoles = setOf(
                        AccountRole.LANDLORD
                    ),
                    propertyRoles = setOf(
                        PropertyRole(
                            propertyId = UUID.randomUUID(),
                            roles = AccountRole.LANDLORD
                        )
                    )
                ),
                PortfolioClaim(
                    portfolioId = UUID.randomUUID(),
                    operationalStatus = OPERATIONAL,
                    portfolioName = "Kambi Portfolio",
                    portfolioImageURL = "https://tajji.io/logo.png",
                    portfolioURL = "https://kambi.tajji.io",
                    billingStatus = STABLE,
                    portfolioRoles = setOf(
                        AccountRole.LANDLORD
                    ),
                    propertyRoles = setOf(
                        PropertyRole(
                            propertyId = UUID.randomUUID(),
                            roles = AccountRole.LANDLORD
                        )
                    )
                )
            )

            context.claims.claim("portfolio_memberships", memberships)
        }
    }
}

data class PortfolioDetails(
    val portfolioName: String,
    val portfolioImageURL: String
)

