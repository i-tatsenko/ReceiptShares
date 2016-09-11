package com.receiptshares.user

import com.receiptshares.user.dao.UserService
import com.receiptshares.user.dao.UserRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
class UserServiceConfiguration {

    def UserRepo userRepo

    @Autowired
    UserServiceConfiguration(UserRepo userRepo) {
        this.userRepo = userRepo
    }

    @Bean
    UserDetailsService userAuthService() {
        return new UserAuthService(userDao());
    }

    @Bean
    UserService userDao() {
        new UserService(userRepo, passwordEncoder())
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
