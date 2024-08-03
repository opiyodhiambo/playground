package com.adventure.auth_server.auth.components

import com.adventure.auth_server.auth.persistence.UsersRepository
import org.slf4j.LoggerFactory
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(private val usersRepository: UsersRepository) : UserDetailsService {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun loadUserByUsername(username: String): UserDetails {
        val user = usersRepository.findByUserName(username = username)
            .orElseThrow { NoSuchElementException("username $username not found") }

        logger.info("username :: ${user.userName}")
        return CustomUserDetails(usersEntity = user)
    }

    fun authenticateNewAccount(emailAddress: String, password: String): Boolean {
        val userDetails = loadUserByUsername(emailAddress)
        return userDetails.password == password
    }
}

