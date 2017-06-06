package com.receiptshares.user.dao

import com.receiptshares.user.model.User
import com.receiptshares.user.registration.EmailNotUniqueException
import com.receiptshares.user.registration.NewUserDTO
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
@CompileStatic
class UserService {

    private UserRepo userRepo
    private PasswordEncoder passwordEncoder
    Closure<String> randomPasswordGenerator = { UUID.randomUUID().toString() }

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
                       .onErrorMap(DataIntegrityViolationException, { error -> new EmailNotUniqueException(newUser.email) })
    }

    Mono<User> getByEmail(String email) {
        return userRepo.findByEmail(email)
                       .map({ found -> found as User })
    }

    Mono<User> getById(String userId) {
        return userRepo.findById(userId)
                       .map({ it as User })
    }
}
