package com.adventure.demo.api

import org.springframework.security.access.prepost.PreFilter
import org.springframework.stereotype.Service

@Service
class TestService {

    @PreFilter("filterObject.owner == authentication.name")
    fun sellProduct(name: MutableList<Product>) {
        TODO("Not yet implemented")
    }

}
