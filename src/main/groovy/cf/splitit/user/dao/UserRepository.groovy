package cf.splitit.user.dao

import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface UserRepository extends ReactiveCrudRepository<UserEntity, String> {

    Mono<UserEntity> findByEmail(String email)

    Flux<UserEntity> findByEmailIn(Collection<String> emails)
}
