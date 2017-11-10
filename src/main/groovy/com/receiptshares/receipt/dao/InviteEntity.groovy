package com.receiptshares.receipt.dao

import com.receiptshares.user.dao.PersonEntity
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "receiptInvites")
class InviteEntity {

    @Id
    String id
    PersonEntity author
    String receiptId
    long creationTime
}
