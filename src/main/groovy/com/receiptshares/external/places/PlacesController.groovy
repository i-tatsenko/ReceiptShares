package com.receiptshares.external.places

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import reactor.core.publisher.Flux

@Controller
@RequestMapping("/v1/place")
class PlacesController {

    PlaceProvider placeProvider

    PlacesController(PlaceProvider placeProvider) {
        this.placeProvider = placeProvider
    }

    @RequestMapping("/suggest")
    @ResponseBody
    Flux<PlaceSuggest> suggest(
            @RequestParam("query") String query, @RequestParam("lat") String lat, @RequestParam("long") String lon) {
        return placeProvider.findPlaces(query, lat, lon)
    }
}
