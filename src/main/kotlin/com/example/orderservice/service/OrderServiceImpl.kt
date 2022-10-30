package com.example.orderservice.service

import com.example.orderservice.dto.OrderDto
import com.example.orderservice.jpa.OrderEntity
import com.example.orderservice.jpa.OrderRepository
import org.modelmapper.ModelMapper
import org.modelmapper.convention.MatchingStrategies
import org.springframework.stereotype.Service
import java.util.*

@Service
class OrderServiceImpl(
    private val orderRepository: OrderRepository,
): OrderService {
    override fun createOrder(orderDto: OrderDto): OrderDto {
        orderDto.orderId = UUID.randomUUID().toString()
        orderDto.totalPrice = orderDto.qty!! * orderDto.unitPrice!!

        val mapper = ModelMapper()
        mapper.configuration.matchingStrategy = MatchingStrategies.STRICT

        val orderEntity = mapper.map(orderDto, OrderEntity::class.java)

        orderRepository.save(orderEntity)

        val returnValue = mapper.map(orderEntity, OrderDto::class.java)

        return returnValue
    }

    override fun getOrderByOrderId(orderId: String): OrderDto {
        val orderEntity = orderRepository.findByOrderId(orderId)
        val orderDto = ModelMapper().map(orderEntity, OrderDto::class.java)
        return orderDto
    }

    override fun getOrdersByUserId(userId: String): Iterable<OrderEntity> {
        return orderRepository.findByUserId(userId)
    }
}