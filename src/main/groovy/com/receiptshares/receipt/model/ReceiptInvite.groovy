package com.receiptshares.receipt.model

import com.receiptshares.DuckTypeConversion
import com.receiptshares.user.model.Person

class ReceiptInvite implements DuckTypeConversion {

    String id
    Person author
    String receiptId
    long creationTime
}
