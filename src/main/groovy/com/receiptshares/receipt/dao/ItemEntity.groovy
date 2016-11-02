package com.receiptshares.receipt.dao

import com.receiptshares.DuckTypeConversion

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "item")
class ItemEntity implements DuckTypeConversion {

    @Id
    @GeneratedValue
    def Long id
    def String name
    def double price
}
