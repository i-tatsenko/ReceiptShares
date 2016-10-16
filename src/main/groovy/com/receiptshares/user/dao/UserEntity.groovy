package com.receiptshares.user.dao

import com.receiptshares.DuckTypeConversion
import groovy.transform.CompileStatic
import groovy.transform.ToString

import javax.persistence.*

@Entity
@Table(name = "user")
@CompileStatic
@ToString
class UserEntity implements DuckTypeConversion {

    @Id
    @GeneratedValue
    def Long id

    def String name

    @Column(unique = true, nullable = false)
    def String email

    def String passwordHash

    def String avatarUrl

}
