package com.receiptshares.user.exceptions

import groovy.transform.CompileStatic

@CompileStatic
class EmailNotUniqueException extends RuntimeException {
    def String email

    def EmailNotUniqueException(String email) {
        this.email = email
    }
}
