package cf.splitit.user.social

import cf.splitit.user.dao.PersonRepository
import cf.splitit.user.dao.UserRepository
import cf.splitit.user.model.Person
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.social.connect.Connection
import org.springframework.social.connect.ConnectionRepository
import org.springframework.social.connect.UsersConnectionRepository
import org.springframework.social.facebook.api.Facebook
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers

@Service
class ConnectionService {

    private ConnectionRepository connectionRepository
    private UsersConnectionRepository userConnectionRepo
    private UserRepository userRepo
    private PersonRepository personRepository


    @Autowired
    ConnectionService(ConnectionRepository connectionRepository, UsersConnectionRepository userConnectionRepo, UserRepository userRepo, PersonRepository personRepository) {
        this.connectionRepository = connectionRepository
        this.userConnectionRepo = userConnectionRepo
        this.userRepo = userRepo
        this.personRepository = personRepository
    }

    Flux<Person> findFriendsForCurrentCustomer() {
        Connection<Facebook> connection = connectionRepository.findPrimaryConnection(Facebook)
        if (!connection) {
            return Flux.empty()
        }
        return Mono.defer({ ->
            Mono.just(connection)
        })
                   .subscribeOn(Schedulers.elastic())
                   .map({ fb -> fb.api.friendOperations().getFriendIds() })
                   .flatMapMany({ ids -> findUserDetailsByConnectionIds(ids) })
    }

    Flux<Person> findUserDetailsByConnectionIds(List<String> providerIds) {
        //TODO refactor provider name
        Set<String> indernalIds = userConnectionRepo.findUserIdsConnectedTo("facebook", providerIds as Set)

        return userRepo.findAllById(indernalIds)
                       .map({ it.person })
                       .map { it as Person }
    }
}
