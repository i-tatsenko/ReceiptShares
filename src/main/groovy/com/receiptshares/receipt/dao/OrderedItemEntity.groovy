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
    String id
    String userId
    String itemId
    String status

    OrderedItemEntity(String ownerId, String itemId) {
        this.userId = ownerId
        this.itemId = itemId
        this.status = ItemStatus.ACTIVE.toString()
    }
}
