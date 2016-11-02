package com.receiptshares.receipt.model

import com.receiptshares.DuckTypeConversion
import com.receiptshares.user.model.User
import groovy.transform.CompileStatic

@CompileStatic
class OrderedItem implements DuckTypeConversion {

    def Long id
    def User user
    def Item item
    def ItemStatus status

}
