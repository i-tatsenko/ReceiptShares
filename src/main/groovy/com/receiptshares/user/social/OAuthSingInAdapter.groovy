package com.receiptshares.user.social

import com.receiptshares.user.dao.UserService
import com.receiptshares.user.model.UserAuthentication
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.social.connect.Connection
import org.springframework.social.connect.web.SignInAdapter
import org.springframework.stereotype.Service
import org.springframework.web.context.request.NativeWebRequest

@Service
class OAuthSingInAdapter implements SignInAdapter {

    UserService userService
    SpringSecurityAuthenticator authenticator

    @Autowired
    OAuthSingInAdapter(UserService userService, SpringSecurityAuthenticator authenticator) {
        this.userService = userService
        this.authenticator = authenticator
    }

    @Override
    String signIn(String userId, Connection<?> connection, NativeWebRequest request) {
        def authenticated = userService.getByEmail(userId)
                                       .map({ new UserAuthentication(it) })
                                       .doOnNext(authenticator.&authenticate)
                                       .hasElement()
                                       .block()
        if (!authenticated)
            throw new IllegalArgumentException("There is no user with email: ${userId}")
        return null
    }

}