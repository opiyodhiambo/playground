package com.adventure.auth_server.auth.components

import com.adventure.auth_server.auth.config.AccountStatus
import org.springframework.stereotype.Component
import java.util.concurrent.ConcurrentHashMap

@Component
class InMemoryUserStore {
    private val users = HashMap<String, Users>()

    fun addUser(emailAddress: String, password: String) {
        users[emailAddress] = Users(emailAddress = emailAddress, password = password)
    }

    fun authenticate(emailAddress: String, password: String): Boolean {
        val user = users[emailAddress] ?: return false
        return user.password == password
    }

    fun getPassword(emailAddress: String): String {
        return users[emailAddress]?.password ?: throw NoSuchElementException("Password not found")
    }

    fun getUser(username: String): Users {
        return users[username] ?: throw NoSuchElementException("User not found")
    }
}

data class Users(
    val emailAddress: String,
    val password: String,
    var accountStatus: AccountStatus = AccountStatus.EMAIL_VERIFICATION_PENDING
)
