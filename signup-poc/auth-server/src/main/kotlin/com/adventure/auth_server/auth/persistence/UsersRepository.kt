package com.adventure.auth_server.auth.persistence

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UsersRepository: JpaRepository<UsersEntity, UUID> {
    fun findByUserName(username: String): Optional<UsersEntity>

}
