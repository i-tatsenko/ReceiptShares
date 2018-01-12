package cf.splitit.user.social

import cf.splitit.user.dao.UserService
import cf.splitit.user.model.User
import cf.splitit.user.model.UserAuthentication
import com.cf.splititres.MockitoExtension
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.springframework.security.web.authentication.RememberMeServices
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
    @Mock
    RememberMeServices rememberMeServices

    @InjectMocks
    OAuthSingInAdapter underTest

    String email = "em@ail.com"
    @Mock
    Connection connection

    @Test
    void shouldSetAuthAndReturnRedirectToRefererWhenEmailWasFound() {
        def user = mock(User)
        def request = mock(NativeWebRequest)
        when(userService.getById(email)).thenReturn(Mono.just(user))

        def result = underTest.signIn(email, null, request)

        verify(authenticator).authenticate(any(UserAuthentication))
        assertThat(result).isEqualTo("/social/signin-callback")
    }

    @Test
    void shouldThrowUnauthorizedErrorWhenNoUserWithSuchEmail() {
        when(userService.getById(email)).thenReturn(Mono.empty())

        def exception = assertThrows(IllegalArgumentException, { underTest.signIn(email, null, null) })
        assertThat(exception.getMessage()).isEqualToIgnoringCase("There is no user with email: ${email}")
    }

}