package cf.splitit.user.social.mongo

import groovy.transform.CompileStatic
import org.springframework.security.crypto.encrypt.TextEncryptor
import org.springframework.social.connect.*

@CompileStatic
class MongoUsersConnectionRepository implements UsersConnectionRepository {

    private final ConnectionService mongoService

    private final ConnectionFactoryLocator connectionFactoryLocator

    private final TextEncryptor textEncryptor

    private ConnectionSignUp connectionSignUp

    MongoUsersConnectionRepository(ConnectionService mongoService,
                                   ConnectionFactoryLocator connectionFactoryLocator,
                                   TextEncryptor textEncryptor) {

        this.mongoService = mongoService
        this.connectionFactoryLocator = connectionFactoryLocator
        this.textEncryptor = textEncryptor
    }

    void setConnectionSignUp(ConnectionSignUp connectionSignUp) {
        this.connectionSignUp = connectionSignUp
    }

    @Override
    List<String> findUserIdsWithConnection(Connection<?> connection) {
        ConnectionKey key = connection.getKey()
        List<String> localUserIds = mongoService.getUserIds(key.getProviderId(), key.getProviderUserId())
        if (localUserIds.size() == 0 && connectionSignUp != null) {
            String newUserId = connectionSignUp.execute(connection)
            if (newUserId != null)
            {
                createConnectionRepository(newUserId).addConnection(connection)
                return Arrays.asList(newUserId)
            }
        }
        return localUserIds
    }

    @Override
    Set<String> findUserIdsConnectedTo(String providerId,
                                       Set<String> providerUserIds) {

        return mongoService.getUserIds(providerId, providerUserIds)
    }

    @Override
    ConnectionRepository createConnectionRepository(String userId) {
        if (userId == null) {
            throw new IllegalArgumentException("userId cannot be null")
        }
        return new MongoConnectionRepository(userId, mongoService, connectionFactoryLocator)
    }

}