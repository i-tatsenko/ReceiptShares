package com.receiptshares.receipt.dao

import com.receiptshares.DuckTypeConversion
import com.receiptshares.user.dao.UserEntity
import groovy.transform.CompileStatic

import javax.persistence.*

@Entity
@Table(name = "place")
@CompileStatic
class PlaceEntity implements DuckTypeConversion {

    @Id
    @GeneratedValue
    def Long id
    def String name
    @OneToOne(targetEntity = UserEntity)
    def UserEntity author
}
