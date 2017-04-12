package com.receiptshares.receipt.dao

import com.receiptshares.DuckTypeConversion
import groovy.transform.CompileStatic
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
@CompileStatic
class ReceiptEntity implements DuckTypeConversion {

    @Id
    BigInteger id
    String name
    BigInteger place
    BigInteger owner
    Set<BigInteger> members
    Set<BigInteger> orderedItems
    String status

}
