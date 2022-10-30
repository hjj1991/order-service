package com.example.orderservice.dto

import java.io.Serializable

class OrderDto: Serializable {
    var productId: String? = null
    var qty: Int? = null
    var unitPrice: Int? = null
    var totalPrice: Int? = null

    var orderId: String? = null
    var userId: String? = null
}