package com.receiptshares.receipt

import com.receiptshares.user.model.User
import groovy.transform.CompileStatic

@CompileStatic
class Receipt {

    def User owner
    def Set<User> members
    def Set<OrderedItem> orderedItems
}
