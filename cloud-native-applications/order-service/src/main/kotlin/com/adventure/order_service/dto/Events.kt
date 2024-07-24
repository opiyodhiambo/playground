package com.adventure.order_service.dto

class Events {

    data class OrderDispatchedMessage(
        val orderId: Long
    )

    data class OrderAcceptedMessage(
        val orderId: Long
    )

}