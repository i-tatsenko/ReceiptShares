package com.receiptshares.user.social

import com.receiptshares.MockitoExtension
import com.receiptshares.user.dao.UserService
import com.receiptshares.user.model.User
import com.receiptshares.user.model.UserAuthentication
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.springframework.social.connect.Connection
import org.springframework.web.context.request.NativeWebRequest
import reactor.core.publisher.Mono

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import static org.junit.jupiter.api.Assertions.assertThrows
import static org.mockito.ArgumentMatchers.any
import static org.mockito.Mockito.*

@ExtendWith(MockitoExtension)
class OAuthSingInAdapterTest {

    @Mock
    UserService userService
    @Mock
    SpringSecurityAuthenticator authenticator

    @InjectMocks
    OAuthSingInAdapter underTest

    String email = "em@ail.com"
    @Mock
    Connection connection

    @Test
    void shouldSetAuthAndReturnNullWhenEmailWasFound() {
        def user = mock(User)
        when(userService.getById(email)).thenReturn(Mono.just(user))

        underTest.signIn(email, null, mock(NativeWebRequest))

        verify(authenticator).authenticate(any(UserAuthentication))
    }

    @Test
    void shouldThrowUnauthorizedErrorWhenNoUserWithSuchEmail() {
        when(userService.getById(email)).thenReturn(Mono.empty())

        def exception = assertThrows(IllegalArgumentException, { underTest.signIn(email, null, null) })
        assertThat(exception.getMessage()).isEqualToIgnoringCase("There is no user with email: ${email}")
    }

}