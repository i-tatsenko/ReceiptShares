package com.receiptshares.user.registration

import groovy.transform.CompileStatic
import groovy.transform.EqualsAndHashCode

@CompileStatic
@EqualsAndHashCode
class NewUserDTO {

    def String name
    def String password
    def String email
}
