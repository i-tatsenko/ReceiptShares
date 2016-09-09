package com.receiptshares.user.registration

import com.receiptshares.external.captcha.CaptchaService
import com.receiptshares.user.dao.UserDao
import com.receiptshares.user.exceptions.EmailNotUniqueException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.AuthenticationException
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.*

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
    def registerNewUser(NewUserDTO newUserDTO, @RequestParam("g-recaptcha-response") String captcha){
        if(!captchaService.verify(captcha)) {
            throw new IllegalArgumentException()
        }
        userDao.registerNewUser(newUserDTO)
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    def login(String username, String password) {
        boolean ok = false
        try {
            def auth = authManager.authenticate(new UsernamePasswordAuthenticationToken(username, password))
             ok = auth.authenticated
        } catch (AuthenticationException ae) {
            ok = false
        }
            return ok ? ResponseEntity.ok().build() : ResponseEntity.notFound().build()
    }

    @ExceptionHandler(EmailNotUniqueException)
    @ResponseStatus(value = HttpStatus.CONFLICT, reason = "Email not unique")
    def notUniqueEmail() {
    }
}