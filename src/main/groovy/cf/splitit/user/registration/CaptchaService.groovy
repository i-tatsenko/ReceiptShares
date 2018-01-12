package cf.splitit.user.registration

import reactor.core.publisher.Mono


interface CaptchaService {

    Mono<Boolean> verify(String token)

}