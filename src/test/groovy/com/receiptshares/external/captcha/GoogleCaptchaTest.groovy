package com.receiptshares.external.captcha

import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.test.web.client.ExpectedCount
import org.springframework.test.web.client.MockRestServiceServer
import org.springframework.web.client.RestTemplate
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
        mockRest.expect(once(), requestTo("${googleCaptcha.BASE_URL}?secret=${secret}&response=${token}"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess("{\"success\": ${result}}", MediaType.APPLICATION_JSON))
        when:
        def actualResult = googleCaptcha.verify(token)
        then:
        mockRest.verify()
        actualResult == result

        where:
        token                        | result
        UUID.randomUUID().toString() | true
        UUID.randomUUID().toString() | false
    }

}
