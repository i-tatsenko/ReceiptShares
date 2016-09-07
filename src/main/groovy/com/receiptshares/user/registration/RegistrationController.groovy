package com.receiptshares.user.registration

import com.receiptshares.user.dao.UserDao
import com.receiptshares.user.exceptions.EmailNotUniqueException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
@RestController
@RequestMapping("/reg")
class RegistrationController {

    public static final Logger LOG = LoggerFactory.getLogger(RegistrationController)

    UserDao userDao
    GoogleCaptcha captchaService

    @Autowired
    RegistrationController(UserDao userDao, GoogleCaptcha captcha) {
        this.userDao = userDao
        this.captchaService = captcha
    }

    @RequestMapping(method = RequestMethod.POST)
    def registerNewUser(NewUserDTO newUserDTO, @RequestParam("g-recaptcha-response") String captcha, HttpServletRequest request){
        if(!captchaService.verify(captcha, request.remoteAddr)) {
            throw new IllegalArgumentException()
        }
        LOG.info("Captcha ${captcha}")
        userDao.registerNewUser(newUserDTO)
    }

    @ExceptionHandler
    def notUniqueEmail(EmailNotUniqueException exc) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body({email: exc.email})
    }
}