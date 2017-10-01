package com.receiptshares.external.places

import com.fasterxml.jackson.databind.ObjectMapper
import com.receiptshares.external.places.swarm.SwarmPlaceProvider
import com.receiptshares.external.places.swarm.SwarmResult
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
    void "should return places suggest"() {
        def expect = [new PlaceSuggest(name: "Cafe", id: "2"), new PlaceSuggest(name: "Bar", id: "42")]
        mockResult(expect)

        def result = underTest.findPlaces("test", null, null)

        expect.each {it.provider = "swarm"}
        StepVerifier.create(result)
                    .expectNextSequence(expect)
                    .expectComplete()
                    .verify()
    }

    private mockResult(Collection<PlaceSuggest> suggestion) {
        def suggestionString = new ObjectMapper().writeValueAsString(suggestion)
        def string = """{"response":{"minivenues": ${suggestionString}}}"""
        mockConnector.stubGet("https://api.foursquare.com/v2/venues/suggestcompletion.*",
                string)

    }

}