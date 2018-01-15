package cf.splitit.user.dao

import cf.splitit.MockitoExtension
import cf.splitit.user.model.User
import cf.splitit.user.registration.EmailNotUniqueException
import cf.splitit.user.registration.NewUserDTO
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.springframework.dao.DuplicateKeyException
import org.springframework.security.crypto.password.PasswordEncoder
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

import static org.junit.Assert.assertEquals
import static org.mockito.ArgumentMatchers.any
import static org.mockito.ArgumentMatchers.anyString
import static org.mockito.Mockito.when

@ExtendWith(MockitoExtension)
class UserServiceTest {

    private static final String PASSWORD_HASH = "encoded password"

    @Mock
    UserRepository userRepo
    @Mock
    PersonRepository personRepository
    @Mock
    PasswordEncoder encoderMock

    def userParams = [name: "user name", email: "email@email.com", password: "some password"]

    @InjectMocks
    UserService underTest

    @BeforeEach
    void setup() {
        when(encoderMock.encode(anyString())).thenReturn(PASSWORD_HASH)
        when(userRepo.save(any(UserEntity))).thenAnswer({ invocation -> Mono.just(invocation.arguments[0]) })
        when(personRepository.save(any(PersonEntity))).thenAnswer({ invocation -> Mono.just(invocation.arguments[0]) })
    }

    @Test
    void "when saving new user password must be encrypted"() {
        Mono<User> result = underTest.registerNewUser(new NewUserDTO(userParams))

        def matcher = StepVerifier.create(result)

        matcher.assertNext({ user ->
            assertEquals(userParams.name, user.person.name)
            assertEquals(userParams.email, user.email)
            assertEquals(PASSWORD_HASH, user.passwordHash)
        })
               .verifyComplete()
    }

    @Test
    void "when saving user with non unique email exception should be thrown"() {
        when(userRepo.save(any(UserEntity))).thenReturn(Mono.error(new DuplicateKeyException(null)))

        def result = StepVerifier.create(underTest.registerNewUser(new NewUserDTO(userParams)))
        result.expectError(EmailNotUniqueException)
              .verify()
    }

    @Test
    void shouldReturnFoundByEmailUser() {
        def params = userParams
        params.remove("password")
        when(userRepo.findByEmail(userParams.email)) thenReturn(
                Mono.just(new UserEntity(person: new PersonEntity(name: userParams.name), email: userParams.email))
        )

        def result = underTest.getByEmail(userParams.email)

        StepVerifier.create(result)
                    .assertNext({ user ->
            assertEquals(userParams.name, user.person.name)
            assertEquals(userParams.email, user.email)
        })
    }

}
