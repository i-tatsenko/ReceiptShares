package com.receiptshares.user.social

import com.receiptshares.user.dao.UserService
import com.receiptshares.user.registration.EmailNotUniqueException
import com.receiptshares.user.registration.NewUserDTO
import groovy.util.logging.Slf4j
import org.springframework.social.connect.Connection
import org.springframework.social.connect.ConnectionSignUp

@Slf4j
class OAuthImplicitRegistration implements ConnectionSignUp {

    UserService userService

    OAuthImplicitRegistration(UserService userService) {
        this.userService = userService
    }

    @Override
    String execute(Connection<?> connection) {
        def profile = connection.fetchUserProfile()
        try {
            def extractor = new AvatarExtractor(api: connection.api)
            def dto = new NewUserDTO(email: profile.email, name: profile.name, avatar: extractor.image)
            userService.registerNewUser(dto)
            return profile.email
        } catch (EmailNotUniqueException enue) {
            log.debug("Can't implicitly register new user", enue)
            return null
        }
    }
}