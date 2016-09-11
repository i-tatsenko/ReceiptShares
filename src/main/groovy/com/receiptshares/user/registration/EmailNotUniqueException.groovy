package com.receiptshares.user.registration

import groovy.transform.CompileStatic

@CompileStatic
class EmailNotUniqueException extends RuntimeException {
    def String email

    def EmailNotUniqueException(String email) {
        this.email = email
    }
}
