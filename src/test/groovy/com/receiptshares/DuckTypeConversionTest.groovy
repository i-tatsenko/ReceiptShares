package com.receiptshares

import com.receiptshares.user.dao.UserEntity
import com.receiptshares.user.model.User
import com.receiptshares.user.registration.NewUserDTO
import spock.lang.Specification

import static java.util.UUID.randomUUID

class DuckTypeConversionTest extends Specification {

    def dto =  new NewUserDTO(email: randomUUID().toString(), name: randomUUID().toString(), password: randomUUID().toString())

    def "should copying all common properties"(){
        when:
        def entity = dto as UserEntity
        then:
        entity.name == dto.name
        entity.email == dto.email
    }

    def "should not try to set readonly props"() {
        when:
        def user = dto as User
        then:
        user.name == dto.name
        user.email == dto.email
    }
}
