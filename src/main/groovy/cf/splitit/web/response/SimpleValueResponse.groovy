package cf.splitit.web.response

import groovy.transform.CompileStatic

@CompileStatic
class SimpleValueResponse<T> {

    T value

    SimpleValueResponse(T value) {
        this.value = value
    }
}
