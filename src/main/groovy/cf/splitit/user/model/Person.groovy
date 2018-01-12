package cf.splitit.user.model

import cf.splitit.DuckTypeConversion
import groovy.transform.CompileStatic

@CompileStatic
class Person implements DuckTypeConversion{
    String id
    String name
    String avatarUrl
}
