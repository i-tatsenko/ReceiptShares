package com.receiptshares.user.dao

import com.receiptshares.user.model.User
import com.receiptshares.user.registration.EmailNotUniqueException
import com.receiptshares.user.registration.NewUserDTO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class UserService {

    def UserRepo userRepo
    def PasswordEncoder passwordEncoder
    def Closure<String> randomPasswordGenerator = { UUID.randomUUID().toString() }

    @Autowired
    UserService(UserRepo repo, PasswordEncoder passwordEncoder) {
        this.userRepo = repo
        this.passwordEncoder = passwordEncoder
    }

    Mono<User> registerNewUser(NewUserDTO newUser) {
        def user = newUser as UserEntity
        user.passwordHash = passwordEncoder.encode(newUser.password ?: randomPasswordGenerator())
        return userRepo.save(user)
                       .map({ u -> u as User })
                       .mapError(DataIntegrityViolationException, { error -> new EmailNotUniqueException(newUser.email) })
    }

    Mono<User> getByEmail(String email) {
        return userRepo.findByEmail(email)
                       .map({ found -> found as User })
    }
}
