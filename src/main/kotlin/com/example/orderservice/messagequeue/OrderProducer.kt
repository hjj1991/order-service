package com.example.orderservice.messagequeue

import com.example.orderservice.dto.*
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

@Service
class OrderProducer(
    private val kafkaTemplate: KafkaTemplate<String, String>,
) {
    private val log = LoggerFactory.getLogger(OrderProducer::class.java)

    val fields: List<Field> = listOf(
        Field("string", true, "order_id"),
        Field("string", true, "user_id"),
        Field("string", true, "product_id"),
        Field("int32", true, "qty"),
        Field("int32", true, "unit_price"),
        Field("int32", true, "total_price"),
    )
    val schema = Schema(
        "struct",
        fields,
        false,
        "orders"
    )

    fun send(topic: String, orderDto: OrderDto): OrderDto {

        val payload = Payload(
            order_id = orderDto.orderId,
            user_id = orderDto.userId,
            product_id = orderDto.productId,
            qty = orderDto.qty,
            unit_price = orderDto.unitPrice,
            total_price = orderDto.totalPrice
        )

        val kafkaOrderDto = KafkaOrderDto(schema, payload)


        val mapper = ObjectMapper()
        var jsonInString = ""
        kotlin.runCatching {
            jsonInString = mapper.writeValueAsString(kafkaOrderDto)
        }.onFailure {
            when(it){
                is JsonProcessingException -> it.printStackTrace()
            }
        }

        kafkaTemplate.send(topic, jsonInString)
        log.info("Order Producer sent data from the Order microservice: ${kafkaOrderDto}")

        return orderDto
    }
}