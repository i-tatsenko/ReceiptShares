package com.receiptshares.receipt.model

import com.receiptshares.DuckTypeConversion
import com.receiptshares.user.model.Person

class Invite implements DuckTypeConversion {

    String id
    Receipt receipt
    Person author
    long creationTime
    boolean alreadyAccepted

}
