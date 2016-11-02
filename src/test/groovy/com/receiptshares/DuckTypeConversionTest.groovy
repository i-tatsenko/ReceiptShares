package com.receiptshares

import com.receiptshares.user.dao.UserEntity
import com.receiptshares.user.model.User
import com.receiptshares.user.registration.NewUserDTO
import spock.lang.Specification

import java.time.DayOfWeek

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

    def "should recursively set all duck typing props"() {
        def testOuterA = "outerA" + randomUUID().toString()
        def testInnerA = 'innerA' + randomUUID().toString()
        given:
        def outerA = new OuterA(str: testOuterA, duck: new InnerA(str: testInnerA))
        when:
        OuterB result = outerA as OuterB
        then:
        result.str == testOuterA
        result.duck.str == testInnerA
    }

    def "enums should be replaced with strings and vice versa"() {
        when:
        def dayString = new DayWithEnum(day: DayOfWeek.MONDAY) as DayWithString
        def dayEnum = new DayWithString(day: "SATURDAY") as DayWithEnum
        then:
        dayString.day == 'MONDAY'
        dayEnum.day == DayOfWeek.SATURDAY
    }

}

class DayWithEnum implements DuckTypeConversion {
    def DayOfWeek day
}

class DayWithString implements DuckTypeConversion {
    def String day
}

class OuterA implements DuckTypeConversion {
    def String str
    def InnerA duck
}

class InnerA implements DuckTypeConversion {
    def String str
}

class OuterB implements DuckTypeConversion {
    def String str
    def InnerB duck
}

class InnerB implements DuckTypeConversion {
    def String str
}