package com.receiptshares.user.dao

import groovy.transform.CompileStatic
import groovy.transform.ToString

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
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
}
