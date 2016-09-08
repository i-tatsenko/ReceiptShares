package com.receiptshares.external.captcha

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@Component
class GoogleCaptcha implements CaptchaService {

    private static final Logger LOG = LoggerFactory.getLogger(GoogleCaptcha)

    static final String BASE_URL = "https://www.google.com/recaptcha/api/siteverify"
    private static final String URL = BASE_URL + "?secret={secret}&response={token}"
    RestTemplate restTemplate

    @Value('${service.captcha.secret}')
    def String secret

    @Autowired
    GoogleCaptcha(RestTemplate restTemplate) {
        this.restTemplate = restTemplate
    }

    def boolean verify(String token) {
        CaptchaResponse result = restTemplate.postForObject(url, null, CaptchaResponse, [secret: secret, token: token])
        LOG.trace("Received from google captcha: ${result}")
        return result.success
    }

    protected String getUrl() {
        return URL
    }
}

