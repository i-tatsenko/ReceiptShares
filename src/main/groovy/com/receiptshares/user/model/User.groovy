package com.receiptshares.user.model

import groovy.transform.CompileStatic
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@CompileStatic
@EqualsAndHashCode(includes = ['email'])
@ToString
class User {

    def String email
    def String name
    def String passwordHash


}
