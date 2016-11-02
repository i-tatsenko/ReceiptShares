package com.receiptshares.receipt.model

import com.receiptshares.DuckTypeConversion
import com.receiptshares.user.model.User
import groovy.transform.CompileStatic

@CompileStatic
class Place implements DuckTypeConversion {

    def Long id
    def User author
    def String name
}
