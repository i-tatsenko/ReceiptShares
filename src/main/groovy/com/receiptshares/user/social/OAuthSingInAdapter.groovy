package com.receiptshares.user.social

import com.receiptshares.user.dao.UserService
import com.receiptshares.user.model.UserAuthentication
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.social.connect.Connection
import org.springframework.social.connect.web.SignInAdapter
import org.springframework.stereotype.Service
import org.springframework.web.context.request.NativeWebRequest
import reactor.core.publisher.Mono


@Service
class OAuthSingInAdapter implements SignInAdapter {

    def UserService userService

    @Autowired
    OAuthSingInAdapter(UserService userService) {
        this.userService = userService
    }

    @Override
    String signIn(String userId, Connection<?> connection, NativeWebRequest request) {
        UserAuthentication authentication = userService.getByEmail(userId).map({ new UserAuthentication(it) }).get()
        if (!authentication)
            throw new IllegalArgumentException("There is no user with email: " + userId)
        SecurityContextHolder.getContext().authentication = authentication
        return null
    }
}
