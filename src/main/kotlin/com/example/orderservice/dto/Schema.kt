package com.example.orderservice.dto

data class Schema(
    var type: String,
    var fields: List<Field>,
    var optional: Boolean,
    var name: String,
) {
}