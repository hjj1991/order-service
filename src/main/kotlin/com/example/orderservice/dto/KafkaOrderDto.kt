package com.example.orderservice.dto

import java.io.Serializable

data class KafkaOrderDto(
    var schema: Schema,
    var payload: Payload,
): Serializable {
}