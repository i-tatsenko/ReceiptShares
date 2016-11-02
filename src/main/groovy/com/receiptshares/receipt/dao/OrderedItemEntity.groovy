package com.receiptshares.receipt.dao

import com.receiptshares.DuckTypeConversion
import com.receiptshares.user.dao.UserEntity

import javax.persistence.*

@Entity
@Table(name = "orderItem")
class OrderedItemEntity implements DuckTypeConversion {

    @Id
    @GeneratedValue
    def Long id
    @ManyToOne(targetEntity = UserEntity)
    def UserEntity user
    @OneToOne(targetEntity = ItemEntity)
    def ItemEntity item
    def String status
}
