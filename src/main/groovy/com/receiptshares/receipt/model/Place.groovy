package com.receiptshares.receipt.model

import com.receiptshares.DuckTypeConversion
import groovy.transform.CompileStatic

@CompileStatic
class Place implements DuckTypeConversion {

    String id
    String provider
    String authorId
    String name
}
