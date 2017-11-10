package com.receiptshares.receipt.model

import com.receiptshares.DuckTypeCollectionMapping
import com.receiptshares.DuckTypeConversion
import com.receiptshares.places.model.Place
import com.receiptshares.user.model.Person
import groovy.transform.CompileStatic

@CompileStatic
class Receipt implements DuckTypeConversion {

    String id
    String name
    Place place
    Person owner
    @DuckTypeCollectionMapping(itemType = Person)
    Set<Person> members
    @DuckTypeCollectionMapping(itemType = OrderedItem)
    Set<OrderedItem> orderedItems
    ReceiptStatus status
    double total
    Map<String, Double> totalsPerMember
    String inviteLink
}
