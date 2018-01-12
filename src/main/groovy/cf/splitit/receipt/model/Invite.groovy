package cf.splitit.receipt.model

import cf.splitit.DuckTypeConversion
import cf.splitit.user.model.Person

class Invite implements DuckTypeConversion {

    String id
    Receipt receipt
    Person author
    long creationTime
    boolean alreadyAccepted

}
