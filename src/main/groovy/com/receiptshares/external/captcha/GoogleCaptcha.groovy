package com.receiptshares.external.captcha

import com.receiptshares.user.registration.CaptchaInvalidException
import com.receiptshares.user.registration.CaptchaService
import groovy.transform.CompileStatic
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Component
@CompileStatic
class GoogleCaptcha implements CaptchaService {

    private static final Logger LOG = LoggerFactory.getLogger(GoogleCaptcha)

    static final String BASE_URL = "https://www.google.com/recaptcha/api/siteverify"
    private static final String URL = BASE_URL + "?secret={secret}&response={token}"
    static final int RETRIES_COUNT = 3

    WebClient webClient = WebClient.create("https://www.google.com/recaptcha/api/siteverify")

    RestTemplate restTemplate
    @Value('${service.captcha.secret}')
    def String secret

    @Autowired
    GoogleCaptcha(RestTemplate restTemplate) {
        this.restTemplate = restTemplate
    }

    Mono<Boolean> verify(String token) {
        return askGoogle(token)
                .doOnNext { LOG.trace("Captcha verified") }
                .map(this.&processResponse)
                .retry(RETRIES_COUNT, { ex -> !(ex instanceof CaptchaInvalidException) })
    }

    private Boolean processResponse(CaptchaResponse response) {
        if (!response.success) {
            throw new CaptchaInvalidException()
        }
        return true
    }

    private Mono<CaptchaResponse> askGoogle(String token) {
//        def result = restTemplate.postForObject(url, null, CaptchaResponse, [secret: secret, token: token])
//        if (!result.success) {
//            throw new CaptchaInvalidException()
//        }
//        return result.success
        return webClient.post()
                        .uri("", [secret: secret, token: token])
                        .accept(MediaType.APPLICATION_JSON)
                        .retrieve()
                        .bodyToMono(CaptchaResponse)
    }

    protected String getUrl() {
        return URL
    }
}

