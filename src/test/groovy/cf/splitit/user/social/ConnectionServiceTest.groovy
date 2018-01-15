package cf.splitit.user.social

import cf.splitit.MockitoExtension
import cf.splitit.user.dao.PersonEntity
import cf.splitit.user.dao.UserEntity
import cf.splitit.user.dao.UserRepository
import cf.splitit.user.model.Person
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.springframework.social.connect.Connection
import org.springframework.social.connect.ConnectionRepository
import org.springframework.social.connect.UsersConnectionRepository
import org.springframework.social.facebook.api.Facebook
import org.springframework.social.facebook.api.FriendOperations
import org.springframework.social.facebook.api.PagedList
import reactor.core.publisher.Flux
import reactor.test.StepVerifier

import static org.mockito.Mockito.when

@ExtendWith(MockitoExtension)
class ConnectionServiceTest {

    @Mock
    ConnectionRepository connectionRepository
    @Mock
    UsersConnectionRepository userConnectionRepo
    @Mock
    UserRepository userRepo

    @InjectMocks
    ConnectionService underTest

    @Mock
    Connection facebookConnection
    @Mock
    Facebook facebookApi
    @Mock
    FriendOperations friendOperations

    def userParams = [
            [id: "1", name: "user name 1"],
            [id: "2", name: "user name 2"],
            [id: "3", name: "user name 3"],
    ]

    def providerIds = ["prov1", "prov2", "prov3"]

    @BeforeEach
    void setup() {
        when(userConnectionRepo.findUserIdsConnectedTo("facebook", providerIds as Set)).thenReturn(["1", "2", "3"] as Set)
        when(userRepo.findAllById(["1", "2", "3"] as Set)).thenReturn(Flux.just(
                new UserEntity(id: "1", person: new PersonEntity(name: "user name 1")),
                new UserEntity(id: "2", person: new PersonEntity(name: "user name 2")),
                new UserEntity(id: "3", person: new PersonEntity(name: "user name 3"))))
    }

    @Test
    void shouldReturnUsersWithProviderIds() {
        Flux<Person> result = underTest.findUserDetailsByConnectionIds(providerIds)
        StepVerifier.create(result)
                    .expectNextSequence(createUsers(Person))
    }

    @Test
    void shouldReturnFacebookFriends() {
        when(connectionRepository.findPrimaryConnection(Facebook)).thenReturn(facebookConnection)
        when(facebookConnection.getApi()).thenReturn(facebookApi)
        when(facebookApi.friendOperations()).thenReturn(friendOperations)
        when(friendOperations.getFriendIds()).thenReturn(new PagedList<String>(providerIds, null, null))

        StepVerifier.create(underTest.findFriendsForCurrentCustomer())
                    .expectNextSequence(createUsers(Person))
    }

    @Test
    void shouldReturnEmptyListWhenThereIsNoFacebookConnection() {
        when(connectionRepository.findPrimaryConnection(Facebook)).thenReturn(null)

        StepVerifier.create(underTest.findFriendsForCurrentCustomer())
                    .verifyComplete()
    }

    private <T> Collection<T> createUsers(Class<T> usersClass) {
        def meta = usersClass.metaClass
        return userParams.collect({ props ->
            def instance = usersClass.newInstance()
            props.each { k, v -> meta.setProperty(instance, k, v) }
            return instance
        })
    }
}