package com.receiptshares.user.social

import com.receiptshares.user.dao.UserConnectionRepo
import com.receiptshares.user.dao.UserRepo
import com.receiptshares.user.model.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.social.connect.Connection
import org.springframework.social.connect.ConnectionRepository
import org.springframework.social.facebook.api.Facebook
import org.springframework.stereotype.Service
import org.springframework.util.CollectionUtils
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers

import static java.util.stream.Collectors.toList

@Service
class ConnectionService {

    def ConnectionRepository connectionRepository
    def UserConnectionRepo userConnectionRepo
    def UserRepo userRepo


    @Autowired
    ConnectionService(ConnectionRepository connectionRepository, UserConnectionRepo userConnectionRepo, UserRepo userRepo) {
        this.connectionRepository = connectionRepository
        this.userConnectionRepo = userConnectionRepo
        this.userRepo = userRepo
    }

    Flux<User> findFriendsForCurrentCustomer() {
        return Mono.defer({ -> connectionRepository.findPrimaryConnection(Facebook) })
                   .subscribeOn(Schedulers.elastic())
                   .flatMap({ connection -> getFacebookFriends(connection.api) })
    }

    private Flux<User> getFacebookFriends(Facebook api) {
        return Mono.just(api.friendOperations())
                   .map { facebook -> facebook.friendProfiles }
                   .map { friends -> friends.collect({ it.id }) }
                   .flatMap({ friendsId -> findUserDetailsByConnectionIds(friendsId) })
    }

    Flux<User> findUserDetailsByConnectionIds(List<String> ids) {
        List<String> emails = userConnectionRepo.findByProviderUserIdIn(ids)
                                                .map({ user -> user.id })
                                                .toStream()
                                                .collect(toList())
        return userRepo.findByEmailIn(emails)
                       .map { it as User }
    }
}
