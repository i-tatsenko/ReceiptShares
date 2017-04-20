package com.receiptshares

import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.client.reactive.ClientHttpConnector
import org.springframework.http.client.reactive.ClientHttpRequest
import org.springframework.http.client.reactive.ClientHttpResponse
import org.springframework.mock.http.client.reactive.MockClientHttpResponse
import reactor.core.publisher.Mono

import java.util.function.Function

class MockClientHttpConnector implements ClientHttpConnector {


    private Map<HttpMethod, Map<String, String>> stubs = new EnumMap<>(HttpMethod)

    @Override
    Mono<ClientHttpResponse> connect(HttpMethod method, URI uri, Function<? super ClientHttpRequest, Mono<Void>> requestCallback) {
        def result = stubs.get(method).get(uri.toString())
        if (!result) {
            throw new IllegalArgumentException("There is no mapping for ${uri.toString()}")
        }
        def response = new MockClientHttpResponse(HttpStatus.OK)
        response.setBody(result)
        return Mono.just(response)
    }

    def mockPost(String uri, String response) {
        stubs.computeIfAbsent(HttpMethod.POST, {method-> [:]})
             .put(uri, response)
    }
}
