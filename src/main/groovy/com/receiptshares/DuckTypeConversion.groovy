package com.receiptshares

trait DuckTypeConversion {

    def asType(Class _class) {
        def result = _class.newInstance()
        def otherMetaClass = _class.metaClass
        this.class
            .metaClass
            .properties
            .collect { it.name }
            .findAll { result.hasProperty(it) }
            .findAll { it != 'class' }
            .each { propName -> result[propName] = (this[propName]).asType(otherMetaClass.getMetaProperty(propName).type)
        }
        return result
    }
}

