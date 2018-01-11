package com.receiptshares.user

import com.receiptshares.user.dao.UserService
import com.receiptshares.user.model.Person
import com.receiptshares.user.model.User
import com.receiptshares.user.registration.CaptchaService
import com.receiptshares.user.registration.NewUserDTO
import com.receiptshares.user.social.ConnectionService
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.social.MissingAuthorizationException
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

import java.security.Principal

@Component
@RestController
@RequestMapping("/v1")
@Slf4j
class UserController {

    private UserService userService
    private CaptchaService captchaService
    private ConnectionService connectionService

    @Autowired
    UserController(UserService userService, CaptchaService captcha, ConnectionService connectionService) {
        this.userService = userService
        this.captchaService = captcha
        this.connectionService = connectionService
    }

    @RequestMapping(value = "/me", method = RequestMethod.GET)
    @ResponseBody
    def me(Mono<Principal> userAuth) {
        return userAuth.map({it as User}).map({it.person}).switchIfEmpty(Mono.error(new MissingAuthorizationException("")))
    }

    @RequestMapping(value = "/open/reg", method = RequestMethod.POST)
    Mono<Person> registerNewUser(NewUserDTO newUserDTO,
                                 @RequestParam("g-recaptcha-response") String captcha) {
        return captchaService.verify(captcha)
                             .then(Mono.defer({ userService.registerNewUser(newUserDTO) }))
                             .map({ it.person })

    }

    @RequestMapping(value = "/friends", method = RequestMethod.GET)
    @ResponseBody
    Flux<Person> friends() {
        return connectionService.findFriendsForCurrentCustomer()
                                .map({ it as Person })
    }


}