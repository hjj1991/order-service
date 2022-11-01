package com.example.orderservice.messagequeue

import com.example.orderservice.dto.OrderDto
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

@Service
class KafkaProducer(
    private val kafkaTemplate: KafkaTemplate<String, String>,
) {
    private val log = LoggerFactory.getLogger(KafkaProducer::class.java)

    fun send(topic: String, orderDto: OrderDto): OrderDto {
        val mapper = ObjectMapper()
        var jsonInString = ""
        kotlin.runCatching {
            jsonInString = mapper.writeValueAsString(orderDto)
        }.onFailure {
            when(it){
                is JsonProcessingException -> it.printStackTrace()
            }
        }

        kafkaTemplate.send(topic, jsonInString)
        log.info("Kafka Producer sent data from the Order microservice: ${orderDto}")

        return orderDto
    }
}