package com.adventure.auth_server.auth.persistence

import com.adventure.auth_server.auth.config.AccountStatus
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.*

@Table
@Entity
class UsersEntity {
    @Id
    @Column(name = "principal_id") var principalId: UUID = UUID.randomUUID()
    @Column(name = "email_address") var userName: String = ""
    @Column(name = "password") var password: String = ""
    @Column(name = "status")
    @Enumerated
    var status: AccountStatus = AccountStatus.EMAIL_VERIFICATION_PENDING
    @Column
    @Enumerated
    var role: AccountRole = AccountRole.LANDLORD

    companion object {
        fun newUser(
            principalId: UUID,
            userName: String,
            password: String,
            status: AccountStatus,
            role: AccountRole
        ) = UsersEntity().apply {
            this.principalId = principalId
            this.userName = userName
            this.password = password
            this.status = status
            this.role = role

        }
    }
}

enum class AccountRole {
    LANDLORD, RESIDENT, MANAGER
}