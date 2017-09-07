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
            .each { setProp(it, result) }
        return result
    }

    def setProp(String propName, def result) {
        try {
            result[propName] = mapSourceProperty(propName, result)
        } catch (ReadOnlyPropertyException re) {
            //do nothing with read-only props
        }
    }

    def private mapSourceProperty(String propName, def result) {
        def otherMetaClass = result.metaClass
        DuckTypeCollectionMapping targetCollectionMapping
        try {
            targetCollectionMapping = result.getClass().getDeclaredField(propName).getAnnotation(DuckTypeCollectionMapping)
        } catch (NoSuchFieldException nsfe) {
            //ignoring no such field
        }
        if (this[propName] != null && this[propName] instanceof Collection && targetCollectionMapping) {
            Collection resultCollection = new ArrayList().asType(otherMetaClass.getMetaProperty(propName).type)
            resultCollection.addAll(this[propName].collect({ it.asType(targetCollectionMapping.itemType()) }))
            return resultCollection
        } else {
            return (this[propName]).asType(otherMetaClass.getMetaProperty(propName).type)
        }
    }


}

