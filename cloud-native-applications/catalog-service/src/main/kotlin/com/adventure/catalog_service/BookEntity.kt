package com.adventure.catalog_service

import org.springframework.data.annotation.*
import java.time.Instant

data class BookEntity(
    val id: Long? = null,
    val isbn: String,
    val title: String,
    val author: String,
    val price: Double,
    val version: Int = 0,
    @CreatedDate
    val createdDate: Instant?,
    @CreatedBy
    val createdBy: String?,
    @LastModifiedDate
    val lastModifiedDate: Instant?,
    @LastModifiedBy
    val lastModifiedBy: String?,

) {
    companion object {
        fun of(isbn: String, title: String, author: String, price: Double): BookEntity {
            return BookEntity(
                id = null,
                isbn = isbn,
                title = title,
                author = author,
                price = price,
                lastModifiedBy = null,
                lastModifiedDate = null,
                createdBy = null,
                createdDate = null,
                version = 0)
        }
    }
}
