package io.tajji.auth_server_dummy

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class AuthServerDummyApplication

fun main(args: Array<String>) {
	runApplication<AuthServerDummyApplication>(*args)
}
