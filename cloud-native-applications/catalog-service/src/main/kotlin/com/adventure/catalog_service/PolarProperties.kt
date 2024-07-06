package com.adventure.catalog_service

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@ConfigurationProperties(prefix = "polar")
@Component
class PolarProperties {

    private var greeting: String? = ""

    fun getGreeting(): String {
        return greeting!!
    }

    fun setGreeting(greeting: String?) {
        this.greeting = greeting
    }
}