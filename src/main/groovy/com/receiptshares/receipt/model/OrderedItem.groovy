package com.receiptshares.receipt.model

import com.receiptshares.DuckTypeConversion
import groovy.transform.CompileStatic

@CompileStatic
class OrderedItem implements DuckTypeConversion {

    String id
    String userId
    String itemId
    ItemStatus status

}
