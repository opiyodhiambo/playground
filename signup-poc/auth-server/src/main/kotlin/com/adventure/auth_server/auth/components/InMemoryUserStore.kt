package com.adventure.auth_server.auth.components

import org.springframework.stereotype.Component
import java.util.concurrent.ConcurrentHashMap

@Component
class InMemoryUserStore {
    private val users = ConcurrentHashMap<String, String>()

    fun addUser(emailAddress: String, password: String) {
        users[emailAddress] = password
    }

    fun authenticate(emailAddress: String, password: String): Boolean {
        return users[emailAddress] == password
    }

    fun getPassword(emailAddress: String): String? {
        return users[emailAddress]
    }
}