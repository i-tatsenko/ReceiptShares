package com.receiptshares.user.dao

import groovy.transform.CompileStatic
import groovy.transform.ToString

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name="UserConnection")
@CompileStatic
@ToString
class UserConnectionEntity {
    @Id
    @Column(name = "userId")
    def String userId
    @Column(name = "providerId")
    def String providerId
    @Column(name = "providerUserId")
    def String providerUserId
    @Column(name = "rank")
    def int rank
    @Column(name = "displayName")
    def String displayName
    @Column(name = "profileUrl")
    def String profileUrl
    @Column(name = "imageUrl")
    def String imageUrl
    @Column(name = "accessToken")
    def String accessToken
    @Column(name = "secret")
    def String secret
    @Column(name = "refreshToken")
    def String refreshToken
    @Column(name = "expireTime")
    def long expireTime
}
