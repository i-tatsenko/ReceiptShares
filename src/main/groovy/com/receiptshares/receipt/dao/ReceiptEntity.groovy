package com.receiptshares.receipt.dao

import com.receiptshares.DuckTypeConversion
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
    String placeId
    String ownerId
    Set<String> memberIds
    Set<String> orderedItemIds
    String status

}
