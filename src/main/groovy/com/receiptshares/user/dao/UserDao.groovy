package com.receiptshares.user.dao

import com.receiptshares.user.exceptions.EmailNotUniqueException
import com.receiptshares.user.model.User
import com.receiptshares.user.registration.NewUserDTO
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class UserDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserDao)

    def Map<String, User> users = new HashMap<>()

    def registerNewUser(NewUserDTO newUser) {
        if (users.containsKey(newUser.email)) {
            throw new EmailNotUniqueException(newUser.email);
        }

        def user = new User(
                email: newUser.email,
                name: newUser.name,
                passwordHash: newUser.password
        )

        users.put(user.email, user)
        LOGGER.debug("users: ${users}")

    }
}
