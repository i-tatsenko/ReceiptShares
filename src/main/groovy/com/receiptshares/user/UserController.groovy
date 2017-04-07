package com.receiptshares.user

import com.receiptshares.user.dao.UserService
import com.receiptshares.user.model.User
import com.receiptshares.user.registration.CaptchaService
import com.receiptshares.user.registration.EmailNotUniqueException
import com.receiptshares.user.registration.NewUserDTO
import com.receiptshares.user.social.ConnectionService
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.*
import org.springframework.web.context.request.async.DeferredResult
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import rx.schedulers.Schedulers

@Component
@RestController
@RequestMapping("/v1")
@Slf4j
class UserController {

    UserService userDao
    CaptchaService captchaService
    ConnectionService connectionService

    @Autowired
    UserController(UserService userDao, CaptchaService captcha, ConnectionService connectionService) {
        this.userDao = userDao
        this.captchaService = captcha
        this.connectionService = connectionService
    }

    @RequestMapping(value = "/me", method = RequestMethod.GET)
    @ResponseBody
    def me(Authentication userAuth) {
        return userAuth.principal
    }

    @RequestMapping(value = "/open/reg", method = RequestMethod.POST)
    Mono<Boolean> registerNewUser(NewUserDTO newUserDTO,
                               @RequestParam("g-recaptcha-response") Mono<String> captcha) {
        return captchaService.verify(captcha)
                             .doOnNext({result->userDao.registerNewUser(newUserDTO)})
    }

    @RequestMapping(value = "/friends", method = RequestMethod.GET)
    @ResponseBody
    Flux<User> friends() {
        return connectionService.findFriendsForCurrentCustomer()
    }

    private static def responseForError(Throwable e) {
        log.debug("Error while registration", e)
        def status = HttpStatus.BAD_REQUEST
        if (e instanceof EmailNotUniqueException)
            status = HttpStatus.CONFLICT
        return ResponseEntity.status(status).build()
    }
}