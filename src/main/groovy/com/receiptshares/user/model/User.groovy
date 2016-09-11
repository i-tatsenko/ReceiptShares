package com.receiptshares.user.model

import com.fasterxml.jackson.annotation.JsonIgnore
import groovy.transform.CompileStatic
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

@CompileStatic
@EqualsAndHashCode(includes = ['email'])
@ToString
class User implements UserDetails {

    def long id
    def String email
    def String name
    @JsonIgnore
    def String passwordHash

    @JsonIgnore
    @Override
    Collection<? extends GrantedAuthority> getAuthorities() {
        return [Role.USER]
    }

    @Override
    @JsonIgnore
    String getPassword() {
        return passwordHash
    }

    @JsonIgnore
    @Override
    String getUsername() {
        return email
    }

    @JsonIgnore
    @Override
    boolean isAccountNonExpired() {
        return true
    }

    @JsonIgnore
    @Override
    boolean isAccountNonLocked() {
        return true
    }

    @JsonIgnore
    @Override
    boolean isCredentialsNonExpired() {
        return true
    }

    @JsonIgnore
    @Override
    boolean isEnabled() {
        return true
    }


}
