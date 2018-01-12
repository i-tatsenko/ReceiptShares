package cf.splitit.places

import cf.splitit.places.model.PlaceSuggest
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import reactor.core.publisher.Flux

import static org.apache.commons.lang3.StringUtils.isBlank
import static org.apache.commons.lang3.StringUtils.isNotBlank

@Controller
@RequestMapping("/v1/place")
class PlacesController {

    PlaceService placeService

    PlacesController(PlaceService placeService) {
        this.placeService = placeService
    }

    @RequestMapping("/suggest")
    @ResponseBody
    Flux<PlaceSuggest> suggest(Authentication auth,
                               @RequestParam("query") String query,
                               @RequestParam(value = "lat", required = false) String lat,
                               @RequestParam(value = "long", required = false) String lon) {
        if (isBlank(query) || query.length() < 3) {
            return Flux.error(new IllegalArgumentException("Query should be more or equal to 3 chars"))
        }
        if (isNotBlank(lat) && isNotBlank(lon)) {
            return placeService.suggestPlacesForUser(query, auth.principal.person.id, lat, lon)
        } else {
            return placeService.suggestPlacesForUser(query, auth.principal.person.id)
        }
    }
}
