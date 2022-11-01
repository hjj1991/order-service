package com.example.orderservice.jpa

import org.hibernate.annotations.ColumnDefault
import java.io.Serializable
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "orders")
class OrderEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long,

    @Column(nullable = false, length = 120)
    var productId: String,
    @Column(nullable = false)
    var qty: Int,
    @Column(nullable = false)
    var unitPrice: Int,
    @Column(nullable = false)
    var totalPrice: Int,

    @Column(nullable = false)
    var userId: String,

    @Column(nullable = false, length = 120)
    var orderId: String,

    @Column(nullable = false, updatable = false, insertable = false)
    @ColumnDefault(value = "CURRENT_TIMESTAMP")
    var createdAt: Date
): Serializable {
}