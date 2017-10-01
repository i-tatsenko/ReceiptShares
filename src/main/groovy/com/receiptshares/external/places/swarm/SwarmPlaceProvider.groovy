package com.receiptshares.external.places.swarm

import com.receiptshares.external.places.PlaceProvider
import com.receiptshares.external.places.PlaceSuggest
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.http.client.reactive.ClientHttpConnector
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux

@Component
@Slf4j
class SwarmPlaceProvider implements PlaceProvider {

    private static final String API_INTEGRATION_DATE = "20170930"

    private static
    final String AUTOCOMPLETE_PATH = "/venues/suggestcompletion?query={query}&ll={ll}&client_id={clientId}&client_secret={clientSecret}&v=${API_INTEGRATION_DATE}"

    @Value('${swarm.app.id}')
    private String appId

    @Value('${swarm.app.secret}')
    private String appSecret

    WebClient webClient

    SwarmPlaceProvider() {
        this(new ReactorClientHttpConnector())
    }

    SwarmPlaceProvider(ClientHttpConnector connector) {
        webClient = WebClient.builder()
                             .clientConnector(connector)
                             .baseUrl("https://api.foursquare.com/v2")
                             .build()
    }

    @Override
    Flux<PlaceSuggest> findPlaces(String query, String latitude, String longitude) {
        WebClient.RequestHeadersSpec uri = webClient.get()
                                                    .uri(AUTOCOMPLETE_PATH, [query: query, ll: "${latitude},${longitude}", clientId: appId, clientSecret: appSecret])
        return uri
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(SwarmResult)
        .doOnNext({println "got $it"})
                .map({ it.response.minivenues })
                .flatMapMany({ Flux.fromIterable(it) })
                .doOnNext({ it.provider = "swarm" })
    }
}
