package com.receiptshares.user

import com.receiptshares.user.dao.UserDao
import com.receiptshares.user.registration.CaptchaService
import com.receiptshares.user.registration.EmailNotUniqueException
import com.receiptshares.user.registration.NewUserDTO
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.*
import org.springframework.web.context.request.async.DeferredResult
import rx.schedulers.Schedulers

@Component
@RestController
@RequestMapping("/v1/open")
class RegistrationController {

    public static final Logger LOG = LoggerFactory.getLogger(RegistrationController)

    UserDao userDao
    CaptchaService captchaService
    AuthenticationManager authManager

    @Autowired
    RegistrationController(UserDao userDao, CaptchaService captcha, AuthenticationManager authManager) {
        this.userDao = userDao
        this.captchaService = captcha
        this.authManager = authManager
    }

    @RequestMapping(value = "/reg", method = RequestMethod.POST)
    DeferredResult<ResponseEntity> registerNewUser(NewUserDTO newUserDTO,
                                                   @RequestParam("g-recaptcha-response") String captcha) {
        def result = new DeferredResult<>()
        captchaService.verify(captcha)
                      .subscribeOn(Schedulers.io())
                      .doOnNext { userDao.registerNewUser(newUserDTO) }
                      .doOnNext { authUser(newUserDTO.email, newUserDTO.password) }
                      .subscribe({ result.setResult(null) },
                { ex -> result.setResult(responseForError(ex)) })

        return result
    }

    private def responseForError(Throwable e) {
        LOG.debug("Error while registration", e)
        def status = HttpStatus.NOT_FOUND
        if (e instanceof EmailNotUniqueException)
            status = HttpStatus.CONFLICT
        return ResponseEntity.status(status).build()
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    def login(String username, String password) {
        boolean ok
        try {
            def auth = authUser(username, password)
            ok = auth.authenticated
        } catch (AuthenticationException ae) {
            ok = false
        }
        return ok ? ResponseEntity.ok().build() : ResponseEntity.notFound().build()
    }

    private Authentication authUser(String username, String password) {
        authManager.authenticate(new UsernamePasswordAuthenticationToken(username, password))
    }

}