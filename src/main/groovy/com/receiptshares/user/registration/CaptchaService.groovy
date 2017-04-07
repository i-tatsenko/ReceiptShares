package com.receiptshares.user.registration

import reactor.core.publisher.Mono


interface CaptchaService {

    Mono<Boolean> verify(Mono<String> token)

}