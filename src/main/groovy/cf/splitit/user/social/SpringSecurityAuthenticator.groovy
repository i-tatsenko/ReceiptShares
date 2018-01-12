package cf.splitit.user.social

import cf.splitit.user.model.UserAuthentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component

@Component
class SpringSecurityAuthenticator {

    def authenticate(UserAuthentication authentication) {
        SecurityContextHolder.getContext().authentication = authentication
    }
}
