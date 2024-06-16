package com.adventure.demo.security
//
//import org.springframework.beans.factory.InitializingBean
//import org.springframework.context.annotation.Bean
//import org.springframework.context.annotation.Configuration
//import org.springframework.scheduling.annotation.EnableAsync
//import org.springframework.security.config.Customizer
//import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
//import org.springframework.security.config.annotation.web.builders.HttpSecurity
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
//import org.springframework.security.core.context.SecurityContextHolder
//import org.springframework.security.web.SecurityFilterChain
//import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
//import org.springframework.security.web.csrf.CsrfFilter
//import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler
//import org.springframework.security.web.csrf.CsrfTokenRequestHandler
//import org.springframework.security.web.server.csrf.ServerCsrfTokenRequestAttributeHandler
//import org.springframework.web.cors.CorsConfiguration
//import org.springframework.web.cors.CorsConfigurationSource
//
//@Configuration
//@EnableMethodSecurity
//open class WebAuthorizationConfig(
//    private val requestValidationFilter: RequestValidationFilter,
//    private val customAuthenticationProvider: CustomAuthenticationProvider,
//    private val customCsrfTokenRepository: CustomCsrfTokenRepository
//) {
//
//    @Bean
//    open fun securityFilterChain(httpSecurity: HttpSecurity): SecurityFilterChain {
//        httpSecurity
//            .addFilterBefore(requestValidationFilter, BasicAuthenticationFilter::class.java)
//            .formLogin(Customizer.withDefaults())
//            .authenticationProvider(customAuthenticationProvider)
//            .csrf {
//                it.csrfTokenRepository(customCsrfTokenRepository)
//                it.csrfTokenRequestHandler(CsrfTokenRequestAttributeHandler())
//            }
//            .cors {
//                val source = CorsConfigurationSource{
//                    val configuration = CorsConfiguration()
//                    configuration.allowedOrigins = listOf("http://localhost:8082", "http://localhost:8083")
//                    configuration.allowedHeaders = listOf("*")
//                    configuration.allowedMethods = listOf("GET")
//                    configuration
//                }
//                it.configurationSource(source)
//            }
//            .authorizeHttpRequests {
//                it
//                    .requestMatchers("/auth/**").permitAll()
//                    .requestMatchers("/transcripts").hasRole("ADMIN")
//                    .anyRequest().authenticated()
//            }
//
//        return httpSecurity.build()
//
//    }
//
//    @Bean
//    open fun initializingBean(): InitializingBean {
//        return InitializingBean {
//            SecurityContextHolder
//                .setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL)
//        }
//    }
//}