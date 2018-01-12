package cf.splitit.external.captcha

import cf.splitit.user.registration.CaptchaInvalidException
import com.cf.splititres.util.MockClientHttpConnector
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import reactor.test.StepVerifier

class GoogleCaptchaTest {

    MockClientHttpConnector mockConnector = new MockClientHttpConnector()

    GoogleCaptcha googleCaptcha = new GoogleCaptcha(mockConnector)
    String secret = UUID.randomUUID().toString()

    @BeforeEach
    void setup() {
        googleCaptcha.secret = secret
    }

    @Test
    void shouldReturnPositiveResultWhenCaptchaIsOk() {
        String token = UUID.randomUUID().toString()
        mockConnector.stubPost(getCaptchaVerifyUri(token), createApiResponse(true))

        def result = googleCaptcha.verify(token)
        StepVerifier.create(result)
                    .expectNext(true)
                    .verifyComplete()
    }

    @Test
    void shouldReturnErrorResultWhenCaptchaIsNotOk() {
        String token = UUID.randomUUID().toString()
        mockConnector.stubPost(getCaptchaVerifyUri(token), createApiResponse(false))

        def result = googleCaptcha.verify(token)
        StepVerifier.create(result)
                    .expectError(CaptchaInvalidException)
                    .verify()
    }

    @Test
    void whenTransportExceptionOccurredRequestWillBeRetried() {
        def token = UUID.randomUUID().toString()
        mockConnector.stubPostErrorThenSuccess(getCaptchaVerifyUri(token), new IOException(), createApiResponse(true))
        mockConnector.stubPost(getCaptchaVerifyUri(token), "{\"success\": true}")

        def result = StepVerifier.create(googleCaptcha.verify(token))

        result.expectNext(true)
              .expectComplete()
              .verify()
    }

    private GString getCaptchaVerifyUri(String token) {
        "${googleCaptcha.BASE_URL}?secret=${secret}&response=${token}"
    }

    private static String createApiResponse(boolean result) {
        "{\"success\": ${result}}"
    }

}
