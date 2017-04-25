package com.receiptshares.user.social

import com.receiptshares.user.dao.UserEntity
import com.receiptshares.user.dao.UserRepo
import com.receiptshares.user.model.User
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.springframework.social.connect.Connection
import org.springframework.social.connect.ConnectionRepository
import org.springframework.social.connect.UsersConnectionRepository
import org.springframework.social.facebook.api.Facebook
import org.springframework.social.facebook.api.FriendOperations
import org.springframework.social.facebook.api.PagedList
import reactor.core.publisher.Flux

import static org.assertj.core.api.Assertions.assertThat
import static org.mockito.Mockito.when

class ConnectionServiceTest {

    @Mock
    ConnectionRepository connectionRepository
    @Mock
    UsersConnectionRepository userConnectionRepo
    @Mock
    UserRepo userRepo

    ConnectionService underTest

    @Mock
    Connection facebookConnection
    @Mock
    Facebook facebookApi
    @Mock
    FriendOperations friendOperations

    def userParams = [
            [id: 1, email: "email1@em.ail"],
            [id: 2, email: "email2@em.ail"],
            [id: 3, email: "email3@em.ail"],
    ]

    def providerIds = ["prov1", "prov2", "prov3"]

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this)
        underTest = new ConnectionService(connectionRepository, userConnectionRepo, userRepo)
        when(userConnectionRepo.findUserIdsConnectedTo("facebook", providerIds as Set)).thenReturn(["1", "2", "3"] as Set)
        when(userRepo.findAll([1L, 2L, 3L])).thenReturn(Flux.just(createUsers(UserEntity).toArray()))
    }

    @Test
    void shouldReturnUsersWithProviderIds() {
        Flux<User> result = underTest.findUserDetailsByConnectionIds(providerIds)
        def actualUsers = result.collectList().block()
        assertThat(actualUsers).containsAll(createUsers(User))
    }

    @Test
    void shouldReturnFacebookFriends() {
        when(connectionRepository.findPrimaryConnection(Facebook)).thenReturn(facebookConnection)
        when(facebookConnection.getApi()).thenReturn(facebookApi)
        when(facebookApi.friendOperations()).thenReturn(friendOperations)
        when(friendOperations.getFriendIds()).thenReturn(new PagedList<String>(providerIds, null, null))

        def result = underTest.findFriendsForCurrentCustomer().collectList().block()
        assertThat(result).containsAll(createUsers(User))
    }

    private <T> Collection<T> createUsers(Class<T> usersClass) {
        def meta = usersClass.metaClass
        return userParams.collect({props->
            def instance = usersClass.newInstance()
            props.each {k, v-> meta.setProperty(instance, k, v)}
            return instance
        })
    }
}