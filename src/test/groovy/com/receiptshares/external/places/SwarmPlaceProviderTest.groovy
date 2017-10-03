package com.receiptshares.external.places

import com.fasterxml.jackson.databind.ObjectMapper
import com.receiptshares.external.places.swarm.SwarmPlaceProvider
import com.receiptshares.util.MockClientHttpConnector
import org.junit.Test
import reactor.test.StepVerifier

class SwarmPlaceProviderTest {

    MockClientHttpConnector mockConnector = new MockClientHttpConnector()
    SwarmPlaceProvider underTest = new SwarmPlaceProvider(mockConnector)

    @Test
    void "should connect to correct url"() {
        mockResult([])

        def result = underTest.findPlaces("test", null, null)
        StepVerifier.create(result)
                    .verifyComplete()
    }

    @Test
    void "should return places suggest sorted by name"() {
        def expect = [[name: "Cafe", id: "2", location: [distance: 3]], [name: "Bar", id: "42", location: [distance: 3]]]
        mockResult(expect)

        def result = underTest.findPlaces("test", null, null)

        expect.each { it.provider = "swarm" }
        StepVerifier.create(result)
                    .expectNextMatches({it.name == "Bar" && it.id == "42"})
                    .expectNextMatches({it.name == "Cafe" && it.id == "2"})
                    .expectComplete()
                    .verify()
    }

    @Test
    void "should return places sorted by distance"() {
        def mockResponse = [[name: "Cafe", id: "1", location: [distance: 20]], [name: "Bar", id: "3", location: [distance: 10]]]
        mockResult(mockResponse)

        def result = underTest.findPlaces("test", null, null)

        StepVerifier.create(result)
                    .expectNextMatches({ it.id == "3" })
                    .expectNextMatches({ it.id == "1" })
                    .expectComplete()
                    .verify()
    }

    private mockResult(Collection suggestion) {
        def suggestionString = new ObjectMapper().writeValueAsString(suggestion)
        def string = """{"response":{"minivenues": ${suggestionString}}}"""
        mockConnector.stubGet("https://api.foursquare.com/v2/venues/suggestcompletion.*",
                string)

    }

}