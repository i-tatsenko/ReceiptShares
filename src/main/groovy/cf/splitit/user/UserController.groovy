package cf.splitit.user

import cf.splitit.user.dao.UserService
import cf.splitit.user.model.Person
import cf.splitit.user.model.User
import cf.splitit.user.registration.CaptchaService
import cf.splitit.user.registration.NewUserDTO
import cf.splitit.user.social.ConnectionService
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
        return (userAuth.principal as User).person
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