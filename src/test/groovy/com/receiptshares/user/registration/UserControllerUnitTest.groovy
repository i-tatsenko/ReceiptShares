package com.receiptshares.user.registration

import com.receiptshares.user.UserController
import com.receiptshares.user.dao.UserService
import com.receiptshares.user.model.User
import com.receiptshares.user.social.ConnectionService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

import static org.junit.Assert.assertEquals
import static org.mockito.ArgumentMatchers.any
import static org.mockito.ArgumentMatchers.anyString
import static org.mockito.Mockito.verifyZeroInteractions
import static org.mockito.Mockito.when

class UserControllerUnitTest {

    @Mock
    UserService userService
    @Mock
    CaptchaService captchaMock
    @Mock
    ConnectionService connectionService

    UserController underTest

    private Map userParams = [name: "userName", email: "userEmail"]

    private NewUserDTO userDto = new NewUserDTO(userParams)

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this)
        when(userService.registerNewUser(userDto)).thenReturn(Mono.just(new User(userParams)))
        when(captchaMock.verify(anyString())).thenReturn(Mono.just(true))
        underTest = new UserController(userService, captchaMock, connectionService)
    }


    @Test
    void when_captcha_ok_and_no_user_with_such_email_registration_ok_and_user_is_authorized() {
        def result = underTest.registerNewUser(userDto, "")

        StepVerifier.create(result)
                    .assertNext({ user ->
            assertEquals(userParams.name, user.name)
            assertEquals(userParams.email, user.email)
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
