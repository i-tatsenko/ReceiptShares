package com.receiptshares.user.model

import groovy.transform.CompileStatic
import groovy.transform.EqualsAndHashCode

@CompileStatic
@EqualsAndHashCode(includes = ['email'])
class User {

    def String email
    def String name
    def String passwordHash


}
