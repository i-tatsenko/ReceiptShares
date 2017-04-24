package com.receiptshares.external.captcha

import com.receiptshares.user.registration.CaptchaInvalidException
import com.receiptshares.util.MockClientHttpConnector
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import reactor.test.StepVerifier

class GoogleCaptchaTest  {

    MockClientHttpConnector mockConnector = new MockClientHttpConnector()

    GoogleCaptcha googleCaptcha = new GoogleCaptcha(mockConnector)
    String secret = UUID.randomUUID().toString()

    @BeforeEach
    void setup() {
        googleCaptcha.secret = secret
    }

    @Test
    void "expect google receive requests in right format"() {
        given:
        mockConnector.stubPost(getCaptchaVerifyUri(token), createApiResponse(result))
        when:
        def actualResult = StepVerifier.create(googleCaptcha.verify(token))
        then:
        if (result) {
            actualResult.expectNext(result)
                        .expectComplete()
                        .verify()
        } else {
            actualResult.expectError(CaptchaInvalidException)
                        .verify()
        }

        where:
        token                        | result
        UUID.randomUUID().toString() | true
        UUID.randomUUID().toString() | false
    }

    @Test
    void "when transport exception occurred request will be retried"() {
        given:
        def token = UUID.randomUUID().toString()
        mockConnector.stubPostErrorThenSuccess(getCaptchaVerifyUri(token), new IOException(), createApiResponse(true))
        mockConnector.stubPost(getCaptchaVerifyUri(token), "{\"success\": true}")

        when:
        def result = StepVerifier.create(googleCaptcha.verify(token))

        then:
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
