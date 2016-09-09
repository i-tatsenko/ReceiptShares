package com.receiptshares.user.model

import groovy.transform.CompileStatic
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

@CompileStatic
@EqualsAndHashCode(includes = ['email'])
@ToString
class User implements UserDetails {

    def String email
    def String name
    def String passwordHash

    @Override
    Collection<? extends GrantedAuthority> getAuthorities() {
        return [Role.USER]
    }

    @Override
    String getPassword() {
        return passwordHash
    }

    @Override
    String getUsername() {
        return email
    }

    @Override
    boolean isAccountNonExpired() {
        return true
    }

    @Override
    boolean isAccountNonLocked() {
        return true
    }

    @Override
    boolean isCredentialsNonExpired() {
        return true
    }

    @Override
    boolean isEnabled() {
        return true
    }
}
