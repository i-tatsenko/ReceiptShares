package com.receiptshares.receipt.model

import com.receiptshares.DuckTypeConversion
import groovy.transform.CompileStatic

@CompileStatic
class Item implements DuckTypeConversion {

    String id
    String name
    double price
}
