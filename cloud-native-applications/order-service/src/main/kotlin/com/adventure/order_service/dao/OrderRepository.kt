package com.adventure.order_service.dao

import org.springframework.data.repository.reactive.ReactiveCrudRepository

interface OrderRepository: ReactiveCrudRepository<Order, Long> {}