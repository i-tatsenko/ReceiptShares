package cf.splitit.user.model

import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority

class UserAuthentication implements Authentication {

    def User user

    UserAuthentication(User user) {
        this.user = user
    }

    @Override
    Collection<? extends GrantedAuthority> getAuthorities() {
        return user.authorities
    }

    @Override
    Object getCredentials() {
        return user.password
    }

    @Override
    Object getDetails() {
        return null
    }

    @Override
    Object getPrincipal() {
        return user
    }

    @Override
    boolean isAuthenticated() {
        return true
    }

    @Override
    void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        //noop
    }

    @Override
    String getName() {
        return user.person.name
    }
}