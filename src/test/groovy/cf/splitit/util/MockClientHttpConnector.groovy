package cf.splitit.util

import groovy.transform.CompileStatic
import org.springframework.http.HttpMethod
import org.springframework.http.client.reactive.ClientHttpConnector
import org.springframework.http.client.reactive.ClientHttpRequest
import org.springframework.http.client.reactive.ClientHttpResponse
import reactor.core.publisher.Mono

import java.util.function.Function
import java.util.regex.Pattern

import static org.springframework.http.HttpMethod.GET
import static org.springframework.http.HttpMethod.POST

@CompileStatic
class MockClientHttpConnector implements ClientHttpConnector {


    private Map<HttpMethod, Map<String, ResponseStub>> stubs = new EnumMap<>(HttpMethod)
//TODO add better support for regexp matching
    @Override
    Mono<ClientHttpResponse> connect(HttpMethod method, URI uri, Function<? super ClientHttpRequest, Mono<Void>> requestCallback) {
        def uriString = uri.toString()
        def registeredStubs = stubs.get(method)
        def result = registeredStubs.find({ u, stub ->
            u == uriString || Pattern.compile(u).matcher(uriString).matches()
        })?.value
        if (!result) {
            throw new IllegalArgumentException("There is no mapping for ${uri.toString()}")
        }
        return result.getNext(method, uri)
    }

    def stubPost(String uri, String response) {
        lookupStubbings(uri, POST).addResponseBody(response)
    }

    def stubGet(String uri, String response) {
        lookupStubbings(uri, GET).addResponseBody(response)
    }


    def stubPostErrorThenSuccess(String uri, Throwable error, String successBody) {
        lookupStubbings(uri, POST).addErrorThenSuccess(error, successBody)
    }

    private Map<String, ResponseStub> getStubsMap(HttpMethod method) {
        return stubs.computeIfAbsent(method, { m -> new HashMap<>() })
    }

    private ResponseStub lookupStubbings(String uri, HttpMethod method) {
        def postMap = getStubsMap(method)
        ResponseStub stub = postMap.get(uri)
        if (!stub) {
            stub = new ResponseStub()
            postMap.put(uri, stub)
        }
        stub
    }


}
