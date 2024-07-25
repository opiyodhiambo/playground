package com.adventure.auth_server.auth.components

import com.adventure.auth_server.auth.config.AccountStatus
import com.adventure.auth_server.auth.persistence.UsersEntity
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class CustomUserDetails(private val usersEntity: UsersEntity) : UserDetails {
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return mutableListOf(
            GrantedAuthority { usersEntity.role.name }
        )
    }

    override fun getPassword() = usersEntity.password

    fun getAccountStatus(): AccountStatus = usersEntity.status

    override fun getUsername(): String = usersEntity.userName

    override fun isEnabled(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true
}
