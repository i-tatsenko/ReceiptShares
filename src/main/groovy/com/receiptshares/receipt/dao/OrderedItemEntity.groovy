package com.receiptshares.receipt.dao

import com.receiptshares.DuckTypeConversion
import com.receiptshares.receipt.model.ItemStatus
import com.receiptshares.user.dao.UserEntity
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

    OrderedItemEntity(UserEntity userEntity, ItemEntity itemEntity) {
        this.user = userEntity.id
        this.item = itemEntity.id
        this.status = ItemStatus.ACTIVE.toString()
    }
}
