package com.receiptshares.user.dao

import com.receiptshares.DuckTypeConversion
import groovy.transform.CompileStatic
import groovy.transform.ToString
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

@CompileStatic
@ToString
@Document
class UserEntity implements DuckTypeConversion {

    @Id
    String id

    String name

    @Indexed(unique = true)
    String email

    String passwordHash

    String avatarUrl

}
