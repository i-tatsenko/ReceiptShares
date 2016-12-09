package com.receiptshares

class Util {

    static def <T> Set<T> toSet(Iterable<T> self) {
        def result = new HashSet<T>()
        self.forEach(result.&add)
        return result
    }

}
