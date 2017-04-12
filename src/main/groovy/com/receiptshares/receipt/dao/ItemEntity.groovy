package com.receiptshares.receipt.dao

import com.receiptshares.DuckTypeConversion
import groovy.transform.CompileStatic
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
@CompileStatic
class ItemEntity implements DuckTypeConversion {

    @Id
    def BigInteger id
    def String name
    def double price
}
