package com.receiptshares.user.social

import com.receiptshares.user.dao.UserService
import com.receiptshares.user.registration.NewUserDTO
import groovy.util.logging.Slf4j
import org.springframework.social.connect.Connection
import org.springframework.social.connect.ConnectionSignUp
import reactor.core.publisher.Mono

@Slf4j
class OAuthImplicitRegistration implements ConnectionSignUp {

    UserService userService

    OAuthImplicitRegistration(UserService userService) {
        this.userService = userService
    }

    @Override
    String execute(Connection<?> connection) {
        def profile = connection.fetchUserProfile()
        def dto = new NewUserDTO(email: profile.email, name: profile.name, avatarUrl: connection.imageUrl)
        return userService.registerNewUser(dto)
                          .map({ user -> user.id })
                          .onErrorResume({ Mono.empty() })
                          .block()
    }
}
