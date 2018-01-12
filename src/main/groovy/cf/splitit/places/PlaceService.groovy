package cf.splitit.places

import cf.splitit.places.dao.PlaceEntity
import cf.splitit.places.dao.repository.PlaceRepository
import cf.splitit.places.model.Place
import cf.splitit.places.model.PlaceSuggest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Component
class PlaceService {

    PlaceRepository placeRepository
    PlaceProvider placeProvider

    @Autowired
    PlaceService(PlaceRepository placeRepository, PlaceProvider placeProvider) {
        this.placeRepository = placeRepository
        this.placeProvider = placeProvider
    }

    Mono<PlaceEntity> findOrCreatePlace(Place place, String authorId) {
        Mono<PlaceEntity> result = Mono.empty()
        if (place.id) {
            result = placeRepository.findByIdAndProvider(place.id, place.provider)
        }
        return result.switchIfEmpty(createNewPlace(place, authorId))
    }

    Mono<PlaceEntity> createNewPlace(Place place, String authorId) {
        Mono<String> image
        if (place.provider == placeProvider.name()) {
            image = placeProvider.getPlaceImage(place.id)
        } else {
            image = Mono.just("")
        }
        return image.map({
            new PlaceEntity(id: place.id, name: place.name, provider: place.provider ?: authorId, imageUrl: it)
        })
                    .flatMap(placeRepository.&save)
    }

    Flux<PlaceSuggest> suggestPlacesForUser(String query, String userId) {
        //TODO add fuzzy search
        return placeRepository.findByProvider(userId)
                              .filter({ it.name.startsWith(query) })
                              .map({ new PlaceSuggest(id: it.id, provider: userId, name: it.name) })
    }

    Flux<PlaceSuggest> suggestPlacesForUser(String query, String userId, String latitude, String longtitude) {
        return suggestPlacesForUser(query, userId).concatWith(
                placeProvider.findPlaces(query, latitude, longtitude).doOnNext({ it.provider = placeProvider.name() }))
    }
}
