package com.receiptshares.user.registration

import com.receiptshares.DuckTypeConversion
import groovy.transform.EqualsAndHashCode

@EqualsAndHashCode
class NewUserDTO implements DuckTypeConversion {

    def String name
    def String password
    def String email
    def byte[] avatar
}
