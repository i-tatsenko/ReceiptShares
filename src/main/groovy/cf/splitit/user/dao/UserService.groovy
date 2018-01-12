package cf.splitit.user.dao

import cf.splitit.user.model.User
import cf.splitit.user.registration.EmailNotUniqueException
import cf.splitit.user.registration.NewUserDTO
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DuplicateKeyException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

import java.util.function.Function

@Component
//@CompileStatic
@Slf4j
class UserService {

    private UserRepository userRepo
    private PersonRepository personRepository
    private PasswordEncoder passwordEncoder
    Closure<String> randomPasswordGenerator = { UUID.randomUUID().toString() }

    @Autowired
    UserService(UserRepository repo, PasswordEncoder passwordEncoder, PersonRepository personRepository) {
        this.userRepo = repo
        this.passwordEncoder = passwordEncoder
        this.personRepository = personRepository
    }

    Mono<User> registerNewUser(NewUserDTO newUser) {
        def person = new PersonEntity(name: newUser.name, avatarUrl: newUser.avatarUrl ?: "/images/no-photo-avatar.svg")
        return personRepository.save(person)
                               .flatMap({ PersonEntity createdPerson -> createUserForPerson(person, newUser.email.toLowerCase(), newUser.password)} as Function)
                               .map({ UserEntity u -> u as User } as Function)
                               .doOnError({ log.error("Can not register user", it) })
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
