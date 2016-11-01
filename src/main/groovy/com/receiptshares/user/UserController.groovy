package com.receiptshares.user

import com.receiptshares.user.dao.UserService
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
    DeferredResult<ResponseEntity> registerNewUser(NewUserDTO newUserDTO,
                                                   @RequestParam("g-recaptcha-response") String captcha) {
        def result = new DeferredResult<>()
        captchaService.verify(captcha)
                      .subscribeOn(Schedulers.io())
                      .doOnNext { userDao.registerNewUser(newUserDTO) }
                      .subscribe({ result.setResult(null) },
                { ex -> result.setResult(responseForError(ex)) })

        return result
    }

    @RequestMapping(value = "/friends", method = RequestMethod.GET)
    @ResponseBody
    DeferredResult friends() {
        def result = new DeferredResult()
        def errorAction = {
            log.debug("Can't get friends", it)
            result.setResult([err: it.message])
        }
        connectionService.findFriendsForCurrentCustomer()
                         .subscribeOn(Schedulers.io())
                         .subscribe({ result.setResult(it) }, errorAction)

        return result
    }

    private static def responseForError(Throwable e) {
        log.debug("Error while registration", e)
        def status = HttpStatus.NOT_FOUND
        if (e instanceof EmailNotUniqueException)
            status = HttpStatus.CONFLICT
        return ResponseEntity.status(status).build()
    }
}