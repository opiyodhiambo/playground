package com.adventure.demo.security

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import java.util.UUID

@Entity
class Token {
    @Id
    @Column(name = "token_id") var tokenId: UUID = UUID.randomUUID()
    @Column(name = "identifier") var identifier: String = ""
    @Column(name = "token") var token: String = ""

}