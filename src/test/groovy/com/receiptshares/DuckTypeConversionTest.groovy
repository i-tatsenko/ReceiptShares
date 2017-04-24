package com.receiptshares

import com.receiptshares.user.dao.UserEntity
import com.receiptshares.user.model.User
import com.receiptshares.user.registration.NewUserDTO
import org.junit.jupiter.api.Test

import java.time.DayOfWeek

import static java.util.UUID.randomUUID

class DuckTypeConversionTest {

    def dto =  new NewUserDTO(email: randomUUID().toString(), name: randomUUID().toString(), password: randomUUID().toString())

    @Test
    void "should copying all common properties"(){
        def entity = dto as UserEntity
        entity.name == dto.name
        entity.email == dto.email
    }

    @Test
    void "should not try to set readonly props"() {
        def user = dto as User
        user.name == dto.name
        user.email == dto.email
    }

    @Test
    void "should recursively set all duck typing props"() {
        def testOuterA = "outerA" + randomUUID().toString()
        def testInnerA = 'innerA' + randomUUID().toString()
        def outerA = new OuterA(str: testOuterA, duck: new InnerA(str: testInnerA))
        OuterB result = outerA as OuterB
        result.str == testOuterA
        result.duck.str == testInnerA
    }

    @Test
    void "enums should be replaced with strings and vice versa"() {
        def dayString = new DayWithEnum(day: DayOfWeek.MONDAY) as DayWithString
        def dayEnum = new DayWithString(day: "SATURDAY") as DayWithEnum
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