package com.receiptshares.user.dao

import com.mongodb.MongoWriteException
import com.receiptshares.user.model.User
import com.receiptshares.user.registration.EmailNotUniqueException
import com.receiptshares.user.registration.NewUserDTO
import groovy.transform.CompileStatic
import groovy.util.logging.Log4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.dao.DuplicateKeyException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

import java.util.function.Function

@Component
@CompileStatic
@Log4j
class UserService {

    private UserRepo userRepo
    private PersonRepository personRepository
    private PasswordEncoder passwordEncoder
    Closure<String> randomPasswordGenerator = { UUID.randomUUID().toString() }

    @Autowired
    UserService(UserRepo repo, PasswordEncoder passwordEncoder, PersonRepository personRepository) {
        this.userRepo = repo
        this.passwordEncoder = passwordEncoder
        this.personRepository = personRepository
    }

    Mono<User> registerNewUser(NewUserDTO newUser) {
        def person = new PersonEntity(name: newUser.name, avatarUrl: newUser.avatarUrl)
        return personRepository.save(person)
                               .flatMap({ PersonEntity createdPerson -> createUserForPerson(person, newUser.email, newUser.password)} as Function)
                               .map({ UserEntity u -> u as User } as Function)
                               .doOnError({ log.error("Blia", it) })
                               .onErrorMap(DuplicateKeyException, { error -> new EmailNotUniqueException(newUser.email) })
    }

    private Mono<UserEntity> createUserForPerson(PersonEntity person, String email, String password) {
        def user = new UserEntity(person: person, email: email, passwordHash: passwordEncoder.encode(password ?: randomPasswordGenerator()))
        return userRepo.save(user)
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
