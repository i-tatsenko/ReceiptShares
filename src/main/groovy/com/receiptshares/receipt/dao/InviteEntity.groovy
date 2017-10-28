package com.receiptshares.receipt.dao

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "receiptInvites")
class InviteEntity {

    @Id
    String id
    String authorId
    String receiptId
    long creationTime
}
