package com.receiptshares.user.registration

import com.receiptshares.user.UserController
import com.receiptshares.user.dao.UserService
import com.receiptshares.user.social.ConnectionService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.context.request.async.DeferredResult
import rx.Observable
import spock.lang.Specification

import static com.receiptshares.TestUtil.waitForResult
import static java.util.UUID.randomUUID

class UserControllerUnitTest extends Specification {

    def userDaoMock = Mock(UserService)
    def captchaMock = Mock(CaptchaService)
    def connectionService = Mock(ConnectionService)
    def underTest = new UserController(userDaoMock, captchaMock, connectionService)
    private NewUserDTO userDto = new NewUserDTO(name: randomUUID().toString(), email: randomUUID().toString(), password: randomUUID().toString())

    def "when captcha ok and no user with such email registration ok and user is authorized"() {
        given:
        captchaMock.verify(_) >> Observable.just(true)
        when:
        def result = underTest.registerNewUser(userDto, "")
        waitForResult(result)
        then:
        1 * userDaoMock.registerNewUser(userDto)
    }

    def "when captcha not ok then no user is created and response is 404"() {
        given:
        captchaMock.verify(_) >> {Observable.error(new CaptchaInvalidException())}
        when:
        DeferredResult<ResponseEntity> result = underTest.registerNewUser(userDto, "")
        waitForResult(result)
        then:
        0 * userDaoMock.registerNewUser(_)
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
    }
}
