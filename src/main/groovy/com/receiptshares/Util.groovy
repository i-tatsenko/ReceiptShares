package com.receiptshares

import java.util.function.BiFunction

class Util {

    static <T> Set<T> toSet(Iterable<T> self) {
        def result = new HashSet<T>()
        self.forEach(result.&add)
        return result
    }

    static <L, R> BiFunction<L, R, L> pickFirst() {
        return {L l, R r -> l}
    }

    static <L, R> BiFunction<L, R, R> pickLast() {
        return {L l, R r -> r}
    }

}
