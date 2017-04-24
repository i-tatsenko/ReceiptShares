package com.receiptshares.user.dao

import com.receiptshares.user.model.User
import com.receiptshares.user.registration.EmailNotUniqueException
import com.receiptshares.user.registration.NewUserDTO
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.security.crypto.password.PasswordEncoder
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

import static org.junit.Assert.assertEquals
import static org.mockito.ArgumentMatchers.any
import static org.mockito.ArgumentMatchers.anyString
import static org.mockito.Mockito.when

class UserServiceTest {

    private static final String PASSWORD_HASH = "encoded password"

    @Mock
    UserRepo userRepo
    @Mock
    PasswordEncoder encoderMock

    def name = "name"
    def email = "email@email.com"

    def underTest

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this)
        when(encoderMock.encode(anyString())).thenReturn(PASSWORD_HASH)
        when(userRepo.save(any(UserEntity))).thenAnswer({ invocation -> Mono.just(invocation.arguments[0]) })
        underTest = new UserService(userRepo, encoderMock)
    }

    @Test
    void "when saving new user password must be encrypted"() {
        Mono<User> result = underTest.registerNewUser(new NewUserDTO(name: name, email: email, password: "some password"))

        def matcher = StepVerifier.create(result)

        matcher.assertNext({ user ->
            assertEquals(name, user.name)
            assertEquals(email, user.email)
            assertEquals(PASSWORD_HASH, user.passwordHash)
        })
               .verifyComplete()
    }

    @Test
    void "when saving user with non unique email exception should be thrown"() {
        when(userRepo.save(any(UserEntity))).thenReturn(Mono.error(new DataIntegrityViolationException(null)))

        def result = StepVerifier.create(underTest.registerNewUser(new NewUserDTO(password: 'somePassword', email: email)))
        result.expectError(EmailNotUniqueException)
              .verify()
    }

}
