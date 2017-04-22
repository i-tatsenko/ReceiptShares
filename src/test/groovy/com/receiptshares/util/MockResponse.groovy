package com.receiptshares.util

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.mock.http.client.reactive.MockClientHttpResponse

class MockResponse extends MockClientHttpResponse {

    private HttpHeaders headers = new HttpHeaders()

    MockResponse(String body) {
        super(HttpStatus.OK)
        setBody(body)
        setContentType(MediaType.APPLICATION_JSON)
    }

    def setContentType(MediaType contentType) {
        headers.setContentType(contentType)
    }

    @Override
    HttpHeaders getHeaders() {
        return headers
    }
}
