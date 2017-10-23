package com.receiptshares.places.dao

import com.receiptshares.DuckTypeConversion
import groovy.transform.CompileStatic
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@CompileStatic
@Document(collection = "places")
class PlaceEntity implements DuckTypeConversion {

    @Id
    def String id
    def String provider
    def String name
}
