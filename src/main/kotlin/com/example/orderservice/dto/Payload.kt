package com.example.orderservice.dto

data class Payload(
    var order_id: String?,
    var user_id: String?,
    var product_id: String?,
    var qty: Int?,
    var unit_price: Int?,
    var total_price: Int?,
) {
}