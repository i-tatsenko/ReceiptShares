package com.receiptshares.user

import com.receiptshares.user.dao.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class UserAuthService implements ReactiveUserDetailsService {

    private UserService userService

    @Autowired
    UserAuthService(UserService userService) {
        this.userService = userService
    }

    @Override
    Mono<UserDetails> findByUsername(String username) {
        return userService.getByEmail(username)
                          .switchIfEmpty(Mono.error(new UsernameNotFoundException("No user with email: ${username}")))
    }
}
