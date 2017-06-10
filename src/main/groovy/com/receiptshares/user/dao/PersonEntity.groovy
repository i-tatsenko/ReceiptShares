package com.receiptshares.user.dao

import groovy.transform.CompileStatic
import groovy.transform.ToString
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@CompileStatic
@ToString
@Document
class PersonEntity {

    @Id
    String id
    String name
    String avatarUrl
}
