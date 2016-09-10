package com.receiptshares.external.captcha

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import rx.Observable

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

    def Observable<Boolean> verify(String token) {
        return Observable.defer { askGoogle(token) }
                         .doOnNext { LOG.trace("Captcha verified") }
                         .retry({ count, exc -> !(exc instanceof CaptchaInvalidException) && count < RETRIES_COUNT })
    }

    private Observable<Boolean> askGoogle(String token) {
        def result = restTemplate.postForObject(url, null, CaptchaResponse, [secret: secret, token: token])
        if (!result.success) {
            throw new CaptchaInvalidException()
        }
        Observable.just true
    }

    protected String getUrl() {
        return URL
    }
}

