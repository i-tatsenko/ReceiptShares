package com.receiptshares.external.captcha

import com.receiptshares.user.registration.CaptchaInvalidException
import com.receiptshares.user.registration.CaptchaService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import reactor.core.publisher.Mono

@Component
class GoogleCaptcha implements CaptchaService {

    private static final Logger LOG = LoggerFactory.getLogger(GoogleCaptcha)

    static final String BASE_URL = "https://www.google.com/recaptcha/api/siteverify"
    private static final String URL = BASE_URL + "?secret={secret}&response={token}"
    static final int RETRIES_COUNT = 3

    RestTemplate restTemplate
    @Value('${service.captcha.secret}')
    def String secret

    @Autowired
    GoogleCaptcha(RestTemplate restTemplate) {
        this.restTemplate = restTemplate
    }

    Mono<Boolean> verify(String token) {
        return Mono.defer({ -> askGoogle(token) })
                   .doOnNext { LOG.trace("Captcha verified") }
                   .retry(RETRIES_COUNT)
                   .map({ result ->
            if (!result) {
                throw new CaptchaInvalidException()
            }
            return result
        })
    }

    private Boolean askGoogle(String token) {
        //TODO migrate to reactive http client
        def result = restTemplate.postForObject(url, null, CaptchaResponse, [secret: secret, token: token])
        return result.success
    }

    protected String getUrl() {
        return URL
    }
}

