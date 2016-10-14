package com.receiptshares

trait DuckTypeConversion {

    def asType(Class _class) {
        def result = _class.newInstance()
        this.class
            .metaClass
            .properties
            .collect { it.name }
            .findAll { result.hasProperty(it) }
            .findAll { it != 'class' }
            .each { setProp(it, result)}
        return result
    }

    def setProp(String propName, def result) {
        def otherMetaClass = result.metaClass
        try {
            result[propName] = (this[propName]).asType(otherMetaClass.getMetaProperty(propName).type)
        } catch (ReadOnlyPropertyException re) {
            //do nothing with read-only props
        }
    }

}

