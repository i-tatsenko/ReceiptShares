package com.receiptshares.user.social

import com.receiptshares.user.dao.UserConnectionRepo
import com.receiptshares.user.dao.UserRepo
import com.receiptshares.user.model.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.social.connect.Connection
import org.springframework.social.connect.ConnectionRepository
import org.springframework.social.facebook.api.Facebook
import org.springframework.stereotype.Service
import rx.Observable

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

    Observable findFriendsForCurrentCustomer() {
        Connection<Facebook> connection = connectionRepository.findPrimaryConnection(Facebook)
        if (!connection)
            return Observable.empty()
        Observable.defer { getFacebookFriends(connection.api) }
    }

    private def getFacebookFriends(Facebook api) {
        def ids = api.friendOperations()
                     .friendProfiles
                     .collect { it.id }
        return Observable.just(findUserDetailsByConnectionIds(ids))
    }

    def findUserDetailsByConnectionIds(List<String> ids) {
        def emails = userConnectionRepo.findByProviderUserIdIn(ids)
                                       .collect { it.userId }
        return userRepo.findByEmailIn(emails)
                .collect { it as User }
    }
}
