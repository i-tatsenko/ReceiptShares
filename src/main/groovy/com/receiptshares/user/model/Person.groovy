package com.receiptshares.user.model

import com.receiptshares.DuckTypeConversion
import groovy.transform.CompileStatic

@CompileStatic
class Person implements DuckTypeConversion{
    String id
    String name
    String avatarUrl
}
