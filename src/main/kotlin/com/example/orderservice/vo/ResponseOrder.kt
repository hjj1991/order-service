package com.example.orderservice.vo

import com.fasterxml.jackson.annotation.JsonInclude
import java.util.Date

@JsonInclude(JsonInclude.Include.NON_NULL)
class ResponseOrder {
    var productId: String? = null
    var qty: Int? = null
    var unitPrice: Int? = null
    var totalPrice: Int? = null
    var stock: Int? = null
    var createdAt: Date? = null

    var orderId: String? = null
}