package com.receiptshares.external.captcha

import com.receiptshares.user.registration.CaptchaInvalidException
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.test.web.client.MockRestServiceServer
import org.springframework.web.client.RestTemplate
import reactor.test.StepVerifier
import spock.lang.Specification

import static org.springframework.test.web.client.ExpectedCount.once
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess

class GoogleCaptchaTest extends Specification {

    RestTemplate restTemplate = new RestTemplate()
    MockRestServiceServer mockRest = MockRestServiceServer.bindTo(restTemplate).build()

    GoogleCaptcha googleCaptcha = new GoogleCaptcha(restTemplate)
    def String secret = UUID.randomUUID().toString()

    def setup() {
        googleCaptcha.secret = secret
    }

    def "expect google receive requests in right format"() {
        given:
        mockRest
        mockRest.expect(once(), requestTo("${googleCaptcha.BASE_URL}?secret=${secret}&response=${token}"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess("{\"success\": ${result}}", MediaType.APPLICATION_JSON))
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
        mockRest.verify()

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
