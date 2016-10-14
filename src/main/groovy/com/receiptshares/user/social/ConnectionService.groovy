package com.receiptshares.user.social

import com.receiptshares.user.dao.UserConnectionRepo
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


    @Autowired
    ConnectionService(ConnectionRepository connectionRepository, UserConnectionRepo userConnectionRepo) {
        this.connectionRepository = connectionRepository
        this.userConnectionRepo = userConnectionRepo
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
        userConnectionRepo.findByProviderUserIdIn(ids)
                          .collect { [email: it.userId, name: it.displayName, image: it.imageUrl] }
    }
}
