package com.receiptshares.user.dao

import com.receiptshares.user.model.User
import groovy.transform.CompileStatic
import groovy.transform.ToString

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "user")
@CompileStatic
@ToString
class UserEntity {

    @Id
    @GeneratedValue
    def Long id

    def String name

    @Column(unique = true)
    def String email

    def String passwordHash

    Object asType(Class<?> _class) {
        if (_class == User) {
            return new User(id: id, name: name, email: email, passwordHash: passwordHash)
        }
    }
}
