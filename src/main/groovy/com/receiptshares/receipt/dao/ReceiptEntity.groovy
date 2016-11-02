package com.receiptshares.receipt.dao

import com.receiptshares.DuckTypeConversion
import com.receiptshares.user.dao.UserEntity

import javax.persistence.*

@Entity
@Table(name = "receipt")
public class ReceiptEntity implements DuckTypeConversion {

    @Id
    @GeneratedValue
    def Long id
    def String name
    @ManyToOne(cascade = CascadeType.ALL)
    def PlaceEntity place
    @ManyToOne
    def UserEntity owner
    @ManyToMany
    def Set<UserEntity> members
    @OneToMany
    def Set<OrderedItemEntity> orderedItems
    @Column(nullable = false)
    def String status

}
