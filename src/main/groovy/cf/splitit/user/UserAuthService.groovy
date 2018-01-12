package cf.splitit.user

import cf.splitit.user.dao.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Component

@Component
class UserAuthService implements UserDetailsService {

    private UserService userService

    @Autowired
    UserAuthService(UserService userService) {
        this.userService = userService
    }

    @Override
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        def user = userService.getByEmail(username).block()
        if (!user)
            throw new UsernameNotFoundException("No user with email: ${username}")
        return user
    }
}
