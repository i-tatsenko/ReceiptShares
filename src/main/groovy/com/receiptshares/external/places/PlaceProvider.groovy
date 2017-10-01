package com.receiptshares.external.places

import reactor.core.publisher.Flux

interface PlaceProvider {

    Flux<PlaceSuggest> findPlaces(String query, String latitude, String longitude)

}