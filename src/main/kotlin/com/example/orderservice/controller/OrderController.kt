package com.example.orderservice.controller

import com.example.orderservice.dto.OrderDto
import com.example.orderservice.messagequeue.KafkaProducer
import com.example.orderservice.messagequeue.OrderProducer
import com.example.orderservice.service.OrderService
import com.example.orderservice.vo.RequestOrder
import com.example.orderservice.vo.ResponseOrder
import org.modelmapper.ModelMapper
import org.modelmapper.convention.MatchingStrategies
import org.slf4j.LoggerFactory
import org.springframework.core.env.Environment
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.lang.Exception
import java.util.*

@RestController
@RequestMapping("/order-service")
class OrderController(
    private val env: Environment,
    private val orderService: OrderService,
    private val kafkaProducer: KafkaProducer,
    private val orderProducer: OrderProducer,
) {

    private val log = LoggerFactory.getLogger(OrderController::class.java)
    @GetMapping("/health_check")
    fun status(): String {
        return "It's Working in Order Service on PORT ${env.getProperty("local.server.port")}"
    }

    @PostMapping("/{userId}/orders")
    fun createOrder(@PathVariable("userId") userId: String, @RequestBody orderDetails: RequestOrder):ResponseEntity<ResponseOrder> {
        log.info("Before add orders data")

        val mapper = ModelMapper()
        mapper.configuration.matchingStrategy = MatchingStrategies.STRICT


        val orderDto = mapper.map(orderDetails, OrderDto::class.java)
        orderDto.userId = userId

        /* jpa */
        val createdOrder = orderService.createOrder(orderDto)
        val responseOrder = mapper.map(createdOrder, ResponseOrder::class.java)


        /* kafka */
//        orderDto.orderId = UUID.randomUUID().toString()
//        orderDto.totalPrice = orderDetails.qty!! * orderDetails.unitPrice!!


        /* send this order to the kafka */
//        kafkaProducer.send("example-catalog-topic", orderDto)
//        orderProducer.send("orders", orderDto)

//        val responseOrder = mapper.map(orderDto, ResponseOrder::class.java)
        log.info("Add add orders data")

        return ResponseEntity.status(HttpStatus.CREATED).body(responseOrder)

    }

    @GetMapping("/{userId}/orders")
    fun getOrder(@PathVariable("userId") userId: String): ResponseEntity<List<ResponseOrder>> {
        log.info("Before add orders data")
        val orderList = orderService.getOrdersByUserId(userId)

        val result = mutableListOf<ResponseOrder>()
        orderList.forEach { v -> result.add(ModelMapper().map(v, ResponseOrder::class.java)) }

        kotlin.runCatching {
            Thread.sleep(1000)
            throw Exception("장애 발생")
        }.onFailure {
            when(it){
                is InterruptedException -> log.error(it.message)
                else -> throw it
            }
        }
        log.info("Add add orders data")
        return ResponseEntity.status(HttpStatus.OK).body(result)
    }

}