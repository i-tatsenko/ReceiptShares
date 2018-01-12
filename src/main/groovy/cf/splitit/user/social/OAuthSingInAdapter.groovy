package cf.splitit.user.social

import cf.splitit.user.dao.UserService
import cf.splitit.user.model.UserAuthentication
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.web.authentication.RememberMeServices
import org.springframework.social.connect.Connection
import org.springframework.social.connect.web.SignInAdapter
import org.springframework.stereotype.Service
import org.springframework.web.context.request.NativeWebRequest

@Service
class OAuthSingInAdapter implements SignInAdapter {

    UserService userService
    SpringSecurityAuthenticator authenticator
    RememberMeServices tokenBasedRememberMeServices

    @Autowired
    OAuthSingInAdapter(UserService userService, SpringSecurityAuthenticator authenticator, RememberMeServices tokenBasedRememberMeServices) {
        this.tokenBasedRememberMeServices = tokenBasedRememberMeServices
        this.userService = userService
        this.authenticator = authenticator
    }

    @Override
    String signIn(String userId, Connection<?> connection, NativeWebRequest request) {
        UserAuthentication authentication = userService.getById(userId)
                                                       .map({ new UserAuthentication(it) })
                                                       .block()
        if (!authentication)
            throw new IllegalArgumentException("There is no user with email: ${userId}")
        authenticator.authenticate(authentication)
        tokenBasedRememberMeServices.loginSuccess(request.getNativeRequest(), request.getNativeResponse(), authentication)
        return "/social/signin-callback"
    }

}