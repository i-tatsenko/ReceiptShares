package com.receiptshares

import com.receiptshares.user.dao.PersonEntity
import com.receiptshares.user.dao.UserEntity
import com.receiptshares.user.model.User
import com.receiptshares.user.registration.NewUserDTO
import org.junit.jupiter.api.Test

import java.time.DayOfWeek

import static java.util.UUID.randomUUID
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat

class DuckTypeConversionTest {

    def dto =  new NewUserDTO(avatarUrl: randomUUID().toString(), name: randomUUID().toString(), password: randomUUID().toString())

    @Test
    void "should copying all common properties"(){
        def entity = dto as PersonEntity
        assertThat(entity.name).isEqualTo(dto.name)
        assertThat(entity.avatarUrl).isEqualTo(dto.avatarUrl)
    }

    @Test
    void "should not try to set readonly props"() {
        def user = dto as PersonEntity
        assertThat(user.name).isEqualTo(dto.name)
        assertThat(user.avatarUrl).isEqualTo(dto.avatarUrl)
    }

    @Test
    void "should recursively set all duck typing props"() {
        def testOuterA = "outerA" + randomUUID().toString()
        def testInnerA = 'innerA' + randomUUID().toString()
        def outerA = new OuterA(str: testOuterA, duck: new InnerA(str: testInnerA))
        OuterB result = outerA as OuterB
        assertThat(result.str).isEqualTo(testOuterA)
        assertThat(result.duck.str).isEqualTo(testInnerA)
    }

    @Test
    void "enums should be replaced with strings and vice versa"() {
        def dayString = new DayWithEnum(day: DayOfWeek.MONDAY) as DayWithString
        def dayEnum = new DayWithString(day: "SATURDAY") as DayWithEnum
        assertThat(dayString.day).isEqualTo('MONDAY')
        assertThat(dayEnum.day).isEqualTo(DayOfWeek.SATURDAY)
    }

    @Test
    void "should set null long to null bigint"() {
        def result = new LongId() as BigIntId
        assertThat(result.id).isNull()
    }

    @Test
    void "shoud set long to big int"() {

    }

}

class DayWithEnum implements DuckTypeConversion {
    DayOfWeek day
}

class DayWithString implements DuckTypeConversion {
    String day
}

class OuterA implements DuckTypeConversion {
    String str
    InnerA duck
}

class InnerA implements DuckTypeConversion {
    String str
}

class OuterB implements DuckTypeConversion {
    String str
    InnerB duck
}

class InnerB implements DuckTypeConversion {
    String str
}

class LongId implements DuckTypeConversion {
    Long id
}

class BigIntId implements DuckTypeConversion {
    String id
}