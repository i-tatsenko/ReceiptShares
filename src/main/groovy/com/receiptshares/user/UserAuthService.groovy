package com.receiptshares.user

import com.receiptshares.user.dao.UserService
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException

class UserAuthService implements UserDetailsService {

    def UserService userService

    UserAuthService(UserService userService) {
        this.userService = userService
    }

    @Override
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userService.getByEmail(username)
                          .orElseThrow({new UsernameNotFoundException("No user with email: ${username}")})
    }
}
