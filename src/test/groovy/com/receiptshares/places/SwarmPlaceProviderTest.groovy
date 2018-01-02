package com.receiptshares.places

import com.fasterxml.jackson.databind.ObjectMapper
import com.receiptshares.external.swarm.SwarmPlaceProvider
import com.receiptshares.util.MockClientHttpConnector
import org.junit.Test
import reactor.test.StepVerifier

class SwarmPlaceProviderTest {

    MockClientHttpConnector mockConnector = new MockClientHttpConnector()
    SwarmPlaceProvider underTest = new SwarmPlaceProvider(mockConnector)

    @Test
    void "should connect to correct url"() {
        mockSuggestResult([])

        def result = underTest.findPlaces("test", null, null)
        StepVerifier.create(result)
                    .verifyComplete()
    }

    @Test
    void "should return places suggest sorted by name"() {
        def expect = [[name: "Cafe", id: "2", location: [distance: 3]], [name: "Bar", id: "42", location: [distance: 3]]]
        mockSuggestResult(expect)

        def result = underTest.findPlaces("test", null, null)

        expect.each { it.provider = "swarm" }
        StepVerifier.create(result)
                    .expectNextMatches({ it.name == "Bar" && it.id == "42" })
                    .expectNextMatches({ it.name == "Cafe" && it.id == "2" })
                    .expectComplete()
                    .verify()
    }

    @Test
    void "should return places sorted by distance"() {
        def mockResponse = [[name: "Cafe", id: "1", location: [distance: 20]], [name: "Bar", id: "3", location: [distance: 10]]]
        mockSuggestResult(mockResponse)

        def result = underTest.findPlaces("test", null, null)

        StepVerifier.create(result)
                    .expectNextMatches({ it.id == "3" })
                    .expectNextMatches({ it.id == "1" })
                    .expectComplete()
                    .verify()
    }

    @Test
    void "should request image"() {
        def host = "www.swarm.com/"
        def path = "/image/test"
        def mockResponse = [bestPhoto: [prefix: host, suffix: path, width: 100, height: 100]]
        def placeId = "placeId"
        mockImageResult(placeId, mockResponse)

        def result = underTest.getPlaceImage(placeId)

        StepVerifier.create(result)
                    .expectNext("${host}100x100${path}".toString())
                    .verifyComplete()
    }

    @Test
    void "should return empty string when there is no image"() {
        mockImageResult("placeId", [:])

        StepVerifier.create(underTest.getPlaceImage("placeId"))
                    .expectNext("")
                    .verifyComplete()
    }

    private mockImageResult(String venueId, responseImage) {
        def imageString = new ObjectMapper().writeValueAsString(responseImage)
        def response = """{"response": {"venue": ${imageString}}}"""
        //TODO
        mockConnector.stubGet("https://api.foursquare.com/v2/venues/${venueId}.*", response)
    }

    private mockSuggestResult(suggestion) {
        def suggestionString = new ObjectMapper().writeValueAsString(suggestion)
        def string = """{"response":{"minivenues": ${suggestionString}}}"""
        mockConnector.stubGet("https://api.foursquare.com/v2/venues/suggestcompletion.*",
                string)

    }

}