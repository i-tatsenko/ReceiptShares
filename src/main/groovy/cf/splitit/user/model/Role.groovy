package cf.splitit.user.model

import org.springframework.security.core.GrantedAuthority

enum Role implements GrantedAuthority {
    USER

    @Override
    String getAuthority() {
        return toString()
    }
}