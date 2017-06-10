package com.receiptshares.user.dao

import com.receiptshares.user.model.User
import groovy.transform.CompileStatic
import groovy.transform.ToString
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

@CompileStatic
@ToString
@Document
class UserEntity {

    @Id
    String id
    @Indexed(unique = true)
    String email
    String passwordHash
    PersonEntity person

    def asType(Class _class) {
        if (!(_class == User)) {
            throw new ClassCastException("Don't know how to cast UserEntity to ${_class.simpleName}")
        }
        return new User(id: id, email: email, name: person.name, avatarUrl: person.avatarUrl, passwordHash: passwordHash)
    }
}
