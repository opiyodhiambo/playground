package com.adventure.order_service.dto

import com.adventure.order_service.datamodel.OrderStatus
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.annotation.Version
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant

@Table("orders")
data class Order(
    @Id
    val id: Long?,
    val bookIsbn: String,
    val bookName: String?,
    val bookPrice: Double?,
    val quantity: Int,
    val status: OrderStatus,
    @CreatedDate
    val createdDate: Instant?,
    @CreatedBy
    val createdBy: String?,
    @LastModifiedDate
    val lastModifiedDate: Instant?,
    @LastModifiedBy
    val lastModifiedBy: String?,
    @Version
    val version: Int
){
    companion object {
        fun of(
            bookIsbn: String,
            bookName: String?,
            bookPrice: Double?,
            quantity: Int,
            status: OrderStatus
        ): Order {
            return Order(
                id = null,
                bookIsbn = bookIsbn,
                bookName = bookName,
                bookPrice = bookPrice,
                quantity = quantity,
                status = status,
                createdDate = null,
                createdBy = null,
                lastModifiedDate = null,
                lastModifiedBy = null,
                version = 0
            )
        }
    }
}
