package com.receiptshares.user.social

import com.receiptshares.user.dao.UserService
import com.receiptshares.user.model.User
import com.receiptshares.user.registration.NewUserDTO
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.springframework.social.connect.Connection
import org.springframework.social.connect.UserProfile
import reactor.core.publisher.Mono

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import static org.mockito.ArgumentMatchers.any
import static org.mockito.Mockito.when

class OAuthImplicitRegistrationTest {

    @Mock
    private UserService userService
    @Mock
    private User registeredUser
    @Mock
    private Connection connection
    @Mock
    private UserProfile userProfile

    private String email = "em@ail.com"

    OAuthImplicitRegistration underTest

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this)
        underTest = new OAuthImplicitRegistration(userService)
        when(connection.fetchUserProfile()).thenReturn(userProfile)
        when(registeredUser.email).thenReturn(email)
    }

    @Test
    void shouldReturnRegisteredUserEmail() {
        when(userService.registerNewUser(any(NewUserDTO))).thenReturn(Mono.just(registeredUser))

        assertThat(underTest.execute(connection)).isEqualTo(email)
    }

    @Test
    void shouldReturnNullWhenRegistrationFailed() {
        when(userService.registerNewUser(any(NewUserDTO))).thenReturn(Mono.error(new RuntimeException()))

        assertThat(underTest.execute(connection)).isNull()
    }

}