package cf.splitit.receipt.model

import cf.splitit.DuckTypeConversion
import cf.splitit.user.model.Person

class ReceiptInvite implements DuckTypeConversion {

    String id
    Person author
    String receiptId
    long creationTime
}
