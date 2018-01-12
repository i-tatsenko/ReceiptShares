package cf.splitit.user

import cf.splitit.user.dao.UserService
import cf.splitit.user.model.User
import com.cf.splititres.MockitoExtension
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.springframework.security.core.userdetails.UsernameNotFoundException
import reactor.core.publisher.Mono

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import static org.mockito.Mockito.when

@ExtendWith(MockitoExtension)
class UserAuthServiceTest {

    @Mock
    UserService userService

    @InjectMocks
    UserAuthService underTest

    String email = "em@ail.com"
    @Mock
    User user

    @Test
    void shouldReturnUser() {
        when(userService.getByEmail(email)).thenReturn(Mono.just(user))

        assertThat(underTest.loadUserByUsername(email)).isEqualTo(user)
    }

    @Test
    void shouldThrwoExceptionWhenNoUserForEmail() {
        when(userService.getByEmail(email)).thenReturn(Mono.empty())

        def thrown = Assertions.assertThrows(UsernameNotFoundException, { underTest.loadUserByUsername(email) })
        assertThat(thrown.getMessage()).isEqualToIgnoringCase("No user with email: ${email}")
    }
}
