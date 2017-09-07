package com.receiptshares.receipt.model

import com.receiptshares.DuckTypeConversion
import com.receiptshares.user.model.Person
import groovy.transform.CompileStatic

@CompileStatic
class OrderedItem implements DuckTypeConversion {

    String id
    Person owner
    Item item
    ItemStatus status

}
