package com.receiptshares.user.registration

import com.receiptshares.user.RegistrationController
import com.receiptshares.user.dao.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.web.context.request.async.DeferredResult
import rx.Observable
import spock.lang.Specification

import static com.receiptshares.TestUtil.waitForResult
import static java.util.UUID.randomUUID

class RegistrationControllerUnitTest extends Specification {

    def userDaoMock = Mock(UserService)
    def captchaMock = Mock(CaptchaService)
    def authManagerMock = Mock(AuthenticationManager)
    def underTest = new RegistrationController(userDaoMock, captchaMock, authManagerMock)
    private NewUserDTO userDto = new NewUserDTO(name: randomUUID().toString(), email: randomUUID().toString(), password: randomUUID().toString())

    def "when captcha ok and no user with such email registration ok and user is authorized"() {
        given:
        captchaMock.verify(_) >> Observable.just(true)
        when:
        def result = underTest.registerNewUser(userDto, "")
        waitForResult(result)
        then:
        1 * userDaoMock.registerNewUser(userDto)
        1 * authManagerMock.authenticate(new UsernamePasswordAuthenticationToken(userDto.email, userDto.password))
    }

    def "when captcha not ok then no user is created and response is 404"() {
        given:
        captchaMock.verify(_) >> {Observable.error(new CaptchaInvalidException())}
        when:
        DeferredResult<ResponseEntity> result = underTest.registerNewUser(userDto, "")
        waitForResult(result)
        then:
        0 * userDaoMock.registerNewUser(_)
        0 * authManagerMock.authenticate(_)
        result.getResult().statusCode == HttpStatus.NOT_FOUND
    }

    def "when captcha ok and there is user with such email, then status is 409"() {
        given:
        captchaMock.verify(_) >> Observable.just(true)
        userDaoMock.registerNewUser(_) >> {throw new EmailNotUniqueException("")}
        when:
        def result = underTest.registerNewUser(new NewUserDTO(), "")
        waitForResult(result)
        then:
        result.getResult().statusCode == HttpStatus.CONFLICT
        0 * authManagerMock.authenticate(_)
    }
}
