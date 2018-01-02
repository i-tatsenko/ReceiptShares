package com.receiptshares.external.swarm

import com.receiptshares.external.swarm.model.SwarmPhoto
import com.receiptshares.external.swarm.model.SwarmSuggestResult
import com.receiptshares.external.swarm.model.SwarmVenueResult
import com.receiptshares.places.PlaceProvider
import com.receiptshares.places.model.PlaceSuggest
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.http.client.reactive.ClientHttpConnector
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

import java.util.function.Function

@Component
@Slf4j
class SwarmPlaceProvider implements PlaceProvider {

    private static final String API_INTEGRATION_DATE = "20171004"

    private static
    final String AUTH_QUERY = "client_id={clientId}&client_secret={clientSecret}&v=${API_INTEGRATION_DATE}"
    private static
    final String AUTOCOMPLETE_PATH = "/venues/suggestcompletion?query={query}&ll={ll}&limit=10&" + AUTH_QUERY
    private static final String VENUE_DETAILS_PATH = "/venues/{venueId}?" + AUTH_QUERY

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
                .bodyToMono(SwarmSuggestResult)
                .map({ it.response.minivenues })
                .flatMapMany({ Flux.fromIterable(it.sort(placeSuggestComparator)) })
    }

    @Override
    Mono<String> getPlaceImage(String placeId) {
        WebClient.RequestHeadersSpec uri = webClient.get()
                                                    .uri(VENUE_DETAILS_PATH, [venueId: placeId, clientId: appId, clientSecret: appSecret])

        return uri.accept(MediaType.APPLICATION_JSON)
                  .retrieve()
                  .bodyToMono(SwarmVenueResult)
        //TODO
//        .filter({it.response.venue?.bestPhoto != null})
                  .map(this.&mapPlaceResponseToImageLink)
    }

    private String mapPlaceResponseToImageLink(SwarmVenueResult result) {
        SwarmPhoto bestPhoto = result.response.venue.bestPhoto
        return "${bestPhoto.prefix}${bestPhoto.width}x${bestPhoto.height}${bestPhoto.suffix}"
    }

    @Override
    String name() {
        return "swarm"
    }
    private Closure placeSuggestComparator = { l, r ->
        return Comparator.comparing({ it.location?.distance } as Function)
                         .thenComparing({ it.name } as Function)
                         .thenComparing({ it.id } as Function)
                         .compare(l, r)
    }
}
