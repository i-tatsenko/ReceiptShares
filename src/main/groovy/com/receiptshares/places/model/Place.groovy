package com.receiptshares.places.model

import com.receiptshares.DuckTypeConversion
import groovy.transform.CompileStatic

@CompileStatic
class Place implements DuckTypeConversion {

    String id
    String provider
    String name
    String imageUrl
}
