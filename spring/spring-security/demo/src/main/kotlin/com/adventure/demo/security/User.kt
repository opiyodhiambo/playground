package com.adventure.demo.security

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import java.util.*

@Entity(name = "users")
class User {
    @Id
    @Column(name = "principal_id") var principalId: UUID = UUID.randomUUID()
    @Column(name = "user_name") var userName: String = ""
    @Column(name = "password") var password: String = ""
    @Column(name = "role") var role: String = ""
}