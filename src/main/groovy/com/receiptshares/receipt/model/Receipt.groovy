package com.receiptshares.receipt.model

import com.receiptshares.DuckTypeConversion
import groovy.transform.CompileStatic

@CompileStatic
class Receipt implements DuckTypeConversion {

    String id
    String name
    String placeId
    String ownerId
    Set<String> memberIds
    Set<String> orderedItemIds
    ReceiptStatus status
}
