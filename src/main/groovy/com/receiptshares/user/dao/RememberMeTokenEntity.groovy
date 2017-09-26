package com.receiptshares.user.dao

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "remembermetokens")
class RememberMeTokenEntity {

    @Id
    String id
    @Indexed(unique = true)
    String username;
    String series;
    String tokenValue;
    Date date;
}
