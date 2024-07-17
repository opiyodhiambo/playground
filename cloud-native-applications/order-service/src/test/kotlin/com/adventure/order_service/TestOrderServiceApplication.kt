package com.adventure.order_service

import org.springframework.boot.fromApplication
import org.springframework.boot.with


fun main(args: Array<String>) {
	fromApplication<OrderServiceApplication>().with(TestcontainersConfiguration::class).run(*args)
}
