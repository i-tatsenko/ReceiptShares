package com.receiptshares.receipt

import com.receiptshares.user.model.User
import groovy.transform.CompileStatic

@CompileStatic
class OrderedItem {

    def User user
    def Item item
}
