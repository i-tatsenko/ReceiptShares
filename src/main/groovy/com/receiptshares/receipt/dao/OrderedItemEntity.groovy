package com.receiptshares.receipt.dao

import com.receiptshares.DuckTypeConversion
import com.receiptshares.receipt.model.ItemStatus
import com.receiptshares.user.dao.PersonEntity
import groovy.transform.CompileStatic
import groovy.transform.InheritConstructors
import groovy.transform.ToString
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "orderedItems")
@CompileStatic
@InheritConstructors
@ToString
class OrderedItemEntity implements DuckTypeConversion {

    @Id
    String id
    PersonEntity owner
    ItemEntity item
    String status

    OrderedItemEntity(PersonEntity owner, ItemEntity item) {
        this.owner = owner
        this.item = item
        this.status = ItemStatus.ACTIVE.toString()
    }
}
