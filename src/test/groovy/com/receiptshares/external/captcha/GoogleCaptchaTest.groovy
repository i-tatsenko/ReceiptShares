package com.receiptshares.external.captcha

import com.receiptshares.MockClientHttpConnector
import com.receiptshares.user.registration.CaptchaInvalidException
import org.springframework.web.client.RestTemplate
import reactor.test.StepVerifier
import spock.lang.Specification

class GoogleCaptchaTest extends Specification {

    MockClientHttpConnector mockConnector = new MockClientHttpConnector()

    GoogleCaptcha googleCaptcha = new GoogleCaptcha(mockConnector)
    def String secret = UUID.randomUUID().toString()

    def setup() {
        googleCaptcha.secret = secret
    }

    def "expect google receive requests in right format"() {
        given:
        mockConnector.mockPost("${googleCaptcha.BASE_URL}?secret=${secret}&response=${token}", "{\"success\": ${result}}")
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

    def "when transport exception occurred request will be retried"() {
        given:
        def restTemplateMock = Mock(RestTemplate)
        googleCaptcha.restTemplate = restTemplateMock
        restTemplateMock.postForObject(*_) >>> [{ throw new IOException() }, new CaptchaResponse(success: true)]

        when:
        def testSubscr = new Object()()
        googleCaptcha.verify("").subscribe(testSubscr)

        then:
        testSubscr.assertNoErrors()
        testSubscr.assertReceivedOnNext([true])
    }

}
