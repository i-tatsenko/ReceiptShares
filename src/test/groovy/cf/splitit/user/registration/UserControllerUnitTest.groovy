package cf.splitit.user.registration

import cf.splitit.user.UserController
import cf.splitit.user.dao.UserService
import cf.splitit.user.model.User
import cf.splitit.user.social.ConnectionService
import com.cf.splititres.MockitoExtension
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

import static org.junit.Assert.assertEquals
import static org.mockito.ArgumentMatchers.any
import static org.mockito.ArgumentMatchers.anyString
import static org.mockito.Mockito.verifyZeroInteractions
import static org.mockito.Mockito.when

@ExtendWith(MockitoExtension)
class UserControllerUnitTest {

    @Mock
    UserService userService
    @Mock
    CaptchaService captchaMock
    @Mock
    ConnectionService connectionService

    @InjectMocks
    UserController underTest

    private Map userParams = [person: [name: "userName"], email: "userEmail"]

    private NewUserDTO userDto = new NewUserDTO([name: "userName", email: "userEmail"])

    @BeforeEach
    void setup() {
        def user = new User(userParams)
        when(userService.registerNewUser(userDto)).thenReturn(Mono.just(user))
        when(captchaMock.verify(anyString())).thenReturn(Mono.just(true))
    }


    @Test
    void when_captcha_ok_and_no_user_with_such_email_registration_ok_and_user_is_authorized() {
        def result = underTest.registerNewUser(userDto, "")

        StepVerifier.create(result)
                    .assertNext({ user ->
            assertEquals(userParams.person.name, user.name)
        }).thenAwait()
                    .verifyComplete()

    }

    @Test
    void when_captcha_not_ok_then_no_user_is_created_and_response_is_404() {
        when(captchaMock.verify(anyString())).thenReturn(Mono.error(new CaptchaInvalidException()))

        def result = underTest.registerNewUser(userDto, "")
        StepVerifier.create(result)
                    .expectError(CaptchaInvalidException)
                    .verify()

        verifyZeroInteractions(userService)
    }

    @Test
    void when_captcha_ok_and_there_is_user_with_such_email_then_status_is_409() {
        when(userService.registerNewUser(any(NewUserDTO))).thenReturn(Mono.error(new EmailNotUniqueException("")))

        def result = underTest.registerNewUser(new NewUserDTO(), "")
        StepVerifier.create(result)
                    .expectError(EmailNotUniqueException)
                    .verify()
    }
}
