package com.receiptshares.receipt.model

import com.receiptshares.DuckTypeConversion
import com.receiptshares.user.model.User
import groovy.transform.CompileStatic

@CompileStatic
class Receipt implements DuckTypeConversion {

    def Long id
    def String name
    def Place place
    def User owner
    def Set<User> members
    def Set<OrderedItem> orderedItems
    def ReceiptStatus status
}
