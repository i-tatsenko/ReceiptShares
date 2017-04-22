package com.receiptshares.util

import org.reactivestreams.Subscriber
import org.springframework.http.HttpMethod
import org.springframework.http.client.reactive.ClientHttpResponse
import reactor.core.publisher.Mono

class ResponseStub {

    List<Closure<Mono<ClientHttpResponse>>> stubbedResponses = new ArrayList<>()
    int lastResponseProducerUsed = -1

    ResponseStub() {
    }

    Mono<ClientHttpResponse> getNext(HttpMethod method, URI uri) {
        return nextResponseProducer()
    }

    def addStubbing(Closure<Mono<ClientHttpResponse>> responseProducer) {
        stubbedResponses.add(responseProducer)
    }

    def addResponseBody(String responseBody) {
        addStubbing {Mono.just(new MockResponse(responseBody))}
    }

    def addErrorThenSuccess(Throwable error, String successBody) {
        addStubbing {new Mono<ClientHttpResponse>() {

            private boolean errorWasFired = false

            @Override
            void subscribe(Subscriber<? super ClientHttpResponse> s) {
                if (!errorWasFired) {
                    s.onError(error)
                    errorWasFired = true
                } else {
                    s.onNext(new MockResponse(successBody))
                }
                s.onComplete()
            }
        }}
    }

    private Closure<Mono<ClientHttpResponse>> getNextResponseProducer() {
        int producerIndex = ++lastResponseProducerUsed % stubbedResponses.size()
        return stubbedResponses[producerIndex]
    }
}
