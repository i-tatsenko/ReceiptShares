package com.receiptshares.user.social

import com.receiptshares.user.dao.UserEntity
import com.receiptshares.user.dao.UserRepo
import com.receiptshares.user.model.User
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.springframework.social.connect.ConnectionRepository
import org.springframework.social.connect.UsersConnectionRepository
import reactor.core.publisher.Flux

import static org.assertj.core.api.Assertions.assertThat
import static org.mockito.ArgumentMatchers.anyList
import static org.mockito.ArgumentMatchers.anyListOf
import static org.mockito.ArgumentMatchers.anySet
import static org.mockito.ArgumentMatchers.eq
import static org.mockito.Mockito.when

class ConnectionServiceTest {

    @Mock
    ConnectionRepository connectionRepository
    @Mock
    UsersConnectionRepository userConnectionRepo
    @Mock
    UserRepo userRepo

    ConnectionService underTest

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this)
        underTest = new ConnectionService(connectionRepository, userConnectionRepo, userRepo)
    }

    def userParams = [
            [id: 1, email: "email1@em.ail"],
            [id: 2, email: "email2@em.ail"],
            [id: 3, email: "email3@em.ail"],
    ]

    @Test
    void shouldReturnUsersWithProviderIds() {
        when(userConnectionRepo.findUserIdsConnectedTo(eq("facebook"), anySet())).thenReturn(["1", "2", "3"] as Set)
        when(userRepo.findAll(anyList())).thenReturn(Flux.just(createUsers(UserEntity).toArray()))

        Flux<User> result = underTest.findUserDetailsByConnectionIds(["prov1", "prov2", "prov3"])
        def actualUsers = result.collectList().block()
        assertThat(actualUsers).containsAll(createUsers(User))
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