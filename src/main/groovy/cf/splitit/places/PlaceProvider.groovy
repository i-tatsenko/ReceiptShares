package cf.splitit.places

import cf.splitit.places.model.PlaceSuggest
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface PlaceProvider {

    Flux<PlaceSuggest> findPlaces(String query, String latitude, String longitude)
    Mono<String> getPlaceImage(String placeId)
    String name()

}