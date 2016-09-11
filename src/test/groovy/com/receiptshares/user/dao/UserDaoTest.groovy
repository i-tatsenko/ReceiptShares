package com.receiptshares.user.dao

import com.receiptshares.user.registration.EmailNotUniqueException
import com.receiptshares.user.registration.NewUserDTO
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.security.crypto.password.PasswordEncoder
import spock.lang.Specification

import static java.util.UUID.randomUUID

class UserDaoTest extends Specification {
    def userRepo = Mock(UserRepo)
    def encoderMock = Mock(PasswordEncoder)
    def userDao = new UserDao(userRepo, encoderMock)

    def setup() {
        encoderMock.encode(_) >> { args -> args[0] + "encoded" }
    }

    def "when saving new user password must be encrypted"() {
        when:
        userDao.registerNewUser(new NewUserDTO(name: name, email: email, password: password))

        then:
        1 * userRepo.save(_) >> { arg->
            def user = arg[0]
            assert user.name == name
            assert user.email == email
            assert user.passwordHash == passwordHash
        }

        where:
        name         | email        | password     | passwordHash
        randomUuid() | randomUuid() | randomUuid() | password + "encoded"
    }

    def "when saving user with non unique email exception should be thrown"() {
        given:
        userRepo.save(_) >> {throw new DataIntegrityViolationException(null)}
        def email = randomUuid()
        when:
        userDao.registerNewUser(new NewUserDTO(password: 'somePassword', email: email))
        then:
        EmailNotUniqueException exc = thrown()
        exc.email == email
    }

    private static String randomUuid() {
        randomUUID().toString()
    }
}
