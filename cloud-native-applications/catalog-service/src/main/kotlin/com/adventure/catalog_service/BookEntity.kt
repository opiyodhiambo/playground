package com.adventure.catalog_service

data class BookEntity(
    val id: Long? = null,
    val isbn: String,
    val title: String,
    val author: String,
    val price: Double,
    val version: Int = 0
) {
    companion object {
        fun of(isbn: String, title: String, author: String, price: Double): BookEntity {
            return BookEntity(id = null, isbn = isbn, title = title, author = author, price = price, version = 0)
        }
    }
}
