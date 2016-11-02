package com.receiptshares.receipt.model

import com.receiptshares.DuckTypeConversion
import groovy.transform.CompileStatic

@CompileStatic
class Item implements DuckTypeConversion {

    def Long id
    def String name
    def double price
}
