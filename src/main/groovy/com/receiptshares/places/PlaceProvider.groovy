package com.receiptshares.places

import com.receiptshares.places.model.PlaceSuggest
import reactor.core.publisher.Flux

interface PlaceProvider {

    Flux<PlaceSuggest> findPlaces(String query, String latitude, String longitude)

}