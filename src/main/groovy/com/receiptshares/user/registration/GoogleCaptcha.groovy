package com.receiptshares.user.registration

import groovy.transform.CompileStatic
import groovy.transform.ToString
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@Component
class GoogleCaptcha {

    private static final Logger LOG = LoggerFactory.getLogger(GoogleCaptcha)

    private static final String URL = "https://www.google.com/recaptcha/api/siteverify"
    RestTemplate restTemplate

    @Value('${service.captcha.secret}')
    private String secret

    @Autowired
    GoogleCaptcha(RestTemplate restTemplate) {
        this.restTemplate = restTemplate
    }

    def verify(String token, def ip) {
        CaptchaResponse result = restTemplate.postForObject(URL,
                new Expando(secret: secret, response: token), CaptchaResponse)
        LOG.info("Received from google captcha: ${result}")
        return result.success
    }
}

@CompileStatic
@ToString
class CaptchaResponse {
    boolean success
    String challenge_ts
    String hostname
}
