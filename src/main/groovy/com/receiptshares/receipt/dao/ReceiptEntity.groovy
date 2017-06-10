package com.receiptshares.receipt.dao

import com.receiptshares.DuckTypeConversion
import com.receiptshares.user.dao.PersonEntity
import groovy.transform.CompileStatic
import groovy.transform.builder.Builder
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
@CompileStatic
@Builder
class ReceiptEntity implements DuckTypeConversion {

    @Id
    String id
    String name
    PlaceEntity place
    PersonEntity owner
    Set<PersonEntity> members
    Set<OrderedItemEntity> orderedItems
    String status

}
