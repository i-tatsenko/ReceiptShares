package com.receiptshares.user.model

import org.springframework.security.core.GrantedAuthority

enum Role implements GrantedAuthority {
    USER

    @Override
    String getAuthority() {
        return toString()
    }
}