package com.receiptshares.external.captcha

import com.receiptshares.user.registration.CaptchaInvalidException
import com.receiptshares.user.registration.CaptchaService
import groovy.transform.CompileStatic
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.http.client.reactive.ClientHttpConnector
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Component
@CompileStatic
class GoogleCaptcha implements CaptchaService {

    private static final Logger LOG = LoggerFactory.getLogger(GoogleCaptcha)
    private static final String BASE_URL = "https://www.google.com/recaptcha/api/siteverify"
    private static final String QUERY_TEMPLATE ="?secret={secret}&response={token}"
    private static final int RETRIES_COUNT = 3

    private final WebClient webClient

    @Value('${service.captcha.secret}')
    private String secret

    GoogleCaptcha() {
        this(new ReactorClientHttpConnector())
    }

    GoogleCaptcha(ClientHttpConnector clientHttpConnector) {
        webClient = WebClient.builder()
                             .clientConnector(clientHttpConnector)
                             .baseUrl(BASE_URL)
                             .build()
    }

    Mono<Boolean> verify(String token) {
        return askGoogle(token)
                .doOnNext { LOG.trace("Captcha verified") }
                .map(this.&processResponse)
    }

    private Boolean processResponse(CaptchaResponse response) {
        if (!response.success) {
            throw new CaptchaInvalidException()
        }
        return true
    }

    private Mono<CaptchaResponse> askGoogle(String token) {
        return webClient.post()
                        .uri(QUERY_TEMPLATE, [secret: secret, token: token])
                        .accept(MediaType.APPLICATION_JSON)
                        .retrieve()
                        .bodyToMono(CaptchaResponse)
                        .retry(RETRIES_COUNT)
    }

}

