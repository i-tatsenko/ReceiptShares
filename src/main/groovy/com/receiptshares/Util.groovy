package com.receiptshares

import com.mongodb.client.result.UpdateResult
import reactor.core.publisher.Mono

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

    static Mono<Void> expectSingleUpdateResult(UpdateResult updateResult) {
        if (updateResult.modifiedCount == 1) {
            return Mono.empty()
        } else {
            return Mono.error(new IllegalStateException("Expected only 1 item to be modified, but got: " + updateResult.modifiedCount + " modified"))
        }
    }
}
