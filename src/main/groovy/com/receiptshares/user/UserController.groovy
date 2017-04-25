package com.receiptshares.user

import com.receiptshares.user.dao.UserService
import com.receiptshares.user.model.User
import com.receiptshares.user.registration.CaptchaService
import com.receiptshares.user.registration.NewUserDTO
import com.receiptshares.user.social.ConnectionService
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

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
    def me(Authentication userAuth) {
        return userAuth.principal
    }

    @RequestMapping(value = "/open/reg", method = RequestMethod.POST)
    Mono<User> registerNewUser(NewUserDTO newUserDTO,
                                  @RequestParam("g-recaptcha-response") String captcha) {
        return captchaService.verify(captcha)
                             .then(Mono.defer({ userService.registerNewUser(newUserDTO) }))

    }

    @RequestMapping(value = "/friends", method = RequestMethod.GET)
    @ResponseBody
    Flux<User> friends() {
        return connectionService.findFriendsForCurrentCustomer()
    }


}