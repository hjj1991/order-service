package com.example.orderservice.dto

data class Field(
    var type: String,
    var optional: Boolean,
    var field: String,
) {
}