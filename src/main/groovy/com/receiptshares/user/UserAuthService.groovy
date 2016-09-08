package com.receiptshares.user

import com.receiptshares.user.dao.UserDao
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Component

@Component
class UserAuthService implements UserDetailsService {

    def UserDao userDao

    UserAuthService(UserDao userDao) {
        this.userDao = userDao
    }

    @Override
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userDao.getByEmail(username)
    }
}
