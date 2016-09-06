package com.receiptshares.user.registration

import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.client.RestTemplate

class GoogleCaptcha {

    private static final String URL = "https://www.google.com/recaptcha/api/siteverify"
    RestTemplate restTemplate

    @Autowired
    GoogleCaptcha(RestTemplate restTemplate) {
        this.restTemplate = restTemplate
    }

    def verify(String token, def ip) {
        CaptchaResponse result = restTemplate.postForObject(URL,
                new Expando(secret: '6Lc2gykTAAAAAJTbDu344PmUi3VLAz3gh7uyTPv-', response: token), CaptchaResponse)
        return result.success
    }
}

@CompileStatic
class CaptchaResponse {
    boolean success
    String challenge_ts
    String hostname
}
