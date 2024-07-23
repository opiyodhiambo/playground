package io.tajji.auth_server_dummy

import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory
import org.springframework.boot.web.servlet.server.ServletWebServerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings

@Configuration
class ServerConfig {

    @Bean
    fun authorizationServerSettings(): AuthorizationServerSettings =
        AuthorizationServerSettings.builder()
            .build()

    @Bean
    fun servletWebServerFactory(): ServletWebServerFactory {
        return TomcatServletWebServerFactory()
    }

}