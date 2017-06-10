package com.receiptshares.receipt.model

import com.receiptshares.DuckTypeConversion
import com.receiptshares.user.dao.PersonEntity
import com.receiptshares.user.model.Person
import groovy.transform.CompileStatic

@CompileStatic
class Receipt implements DuckTypeConversion {

    String id
    String name
    Place place
    Person owner
    Set<Person> members
    Set<OrderedItem> orderedItems
    ReceiptStatus status
}
