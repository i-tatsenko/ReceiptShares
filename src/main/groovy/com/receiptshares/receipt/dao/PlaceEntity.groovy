package com.receiptshares.receipt.dao

import com.receiptshares.DuckTypeConversion
import groovy.transform.CompileStatic
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@CompileStatic
@Document(collection = "places")
class PlaceEntity implements DuckTypeConversion {

    @Id
    def String id
    def String name
    def String authorId
}
