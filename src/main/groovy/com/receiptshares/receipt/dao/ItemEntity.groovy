package com.receiptshares.receipt.dao

import com.receiptshares.DuckTypeConversion
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "items")
class ItemEntity implements DuckTypeConversion {

    @Id
    String id
    String name
    double price
}
