package com.receiptshares.user.social

import com.receiptshares.user.dao.UserRepo
import com.receiptshares.user.model.User
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
    private UserRepo userRepo


    @Autowired
    ConnectionService(ConnectionRepository connectionRepository, UsersConnectionRepository userConnectionRepo, UserRepo userRepo) {
        this.connectionRepository = connectionRepository
        this.userConnectionRepo = userConnectionRepo
        this.userRepo = userRepo
    }

    Flux<User> findFriendsForCurrentCustomer() {
        Connection<Facebook> connection = connectionRepository.findPrimaryConnection(Facebook)
        return Mono.defer({ ->
            Mono.just(connection) })
                   .subscribeOn(Schedulers.elastic())
                   .map({ fb -> fb.api.friendOperations().getFriendIds() })
                   .flatMapMany({ ids -> findUserDetailsByConnectionIds(ids) })
    }

    Flux<User> findUserDetailsByConnectionIds(List<String> providerIds) {
        //TODO refactor provider name
        Set<String> indernalIds = userConnectionRepo.findUserIdsConnectedTo("facebook", providerIds as Set)

        return userRepo.findAllById(indernalIds)
                       .map { it as User }
    }
}
