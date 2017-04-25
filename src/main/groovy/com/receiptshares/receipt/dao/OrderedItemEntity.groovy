package com.receiptshares.receipt.dao

import com.receiptshares.DuckTypeConversion
import com.receiptshares.receipt.model.ItemStatus
import groovy.transform.CompileStatic
import groovy.transform.InheritConstructors
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
@CompileStatic
@InheritConstructors
class OrderedItemEntity implements DuckTypeConversion {

    @Id
    BigInteger id
    BigInteger user
    BigInteger item
    String status

    OrderedItemEntity(BigInteger ownerId, BigInteger itemId) {
        this.user = ownerId
        this.item = itemId
        this.status = ItemStatus.ACTIVE.toString()
    }
}
