package com.receiptshares.receipt.model

import com.receiptshares.DuckTypeConversion

class Invite implements DuckTypeConversion {

    String id
    String receiptId
    String authorId
    long creationTime

}
