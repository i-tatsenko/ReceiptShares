package com.receiptshares.util

import groovy.transform.CompileStatic
import org.springframework.http.HttpMethod
import org.springframework.http.client.reactive.ClientHttpConnector
import org.springframework.http.client.reactive.ClientHttpRequest
import org.springframework.http.client.reactive.ClientHttpResponse
import reactor.core.publisher.Mono

import java.util.function.Function

@CompileStatic
class MockClientHttpConnector implements ClientHttpConnector {


    private Map<HttpMethod, Map<String, ResponseStub>> stubs = new EnumMap<>(HttpMethod)

    @Override
    Mono<ClientHttpResponse> connect(HttpMethod method, URI uri, Function<? super ClientHttpRequest, Mono<Void>> requestCallback) {
        def result = stubs.get(method).get(uri.toString())
        if (!result) {
            throw new IllegalArgumentException("There is no mapping for ${uri.toString()}")
        }
        return result.getNext(method, uri)
    }

    def stubPost(String uri, String response) {
        getPostStubbing(uri).addResponseBody(response)
    }

    private ResponseStub getPostStubbing(String uri) {
        def postMap = getStubsMap(HttpMethod.POST)
        ResponseStub stub = postMap.get(uri)
        if (!stub) {
            stub = new ResponseStub()
            postMap.put(uri, stub)
        }
        stub
    }

    def stubPostErrorThenSuccess(String uri, Throwable error, String successBody) {
        getPostStubbing(uri).addErrorThenSuccess(error, successBody)
    }

    private Map<String, ResponseStub> getStubsMap(HttpMethod method) {
        return stubs.computeIfAbsent(method, { m -> new HashMap<>() })
    }


}
