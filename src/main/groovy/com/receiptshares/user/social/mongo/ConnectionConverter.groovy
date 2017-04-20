package com.receiptshares.user.social.mongo

import groovy.transform.CompileStatic
import org.springframework.security.crypto.encrypt.TextEncryptor
import org.springframework.social.connect.Connection
import org.springframework.social.connect.ConnectionData
import org.springframework.social.connect.ConnectionFactory
import org.springframework.social.connect.ConnectionFactoryLocator

@CompileStatic
class ConnectionConverter {
    private final ConnectionFactoryLocator connectionFactoryLocator
    private final TextEncryptor textEncryptor

    ConnectionConverter(ConnectionFactoryLocator connectionFactoryLocator,
                        TextEncryptor textEncryptor) {

        this.connectionFactoryLocator = connectionFactoryLocator
        this.textEncryptor = textEncryptor
    }

    Connection<?> convert(MongoConnection cnn) {
        if (cnn==null) return null

        ConnectionData connectionData = fillConnectionData(cnn)
        ConnectionFactory<?> connectionFactory = connectionFactoryLocator.getConnectionFactory(connectionData.getProviderId())
        return connectionFactory.createConnection(connectionData)
    }

    private ConnectionData fillConnectionData(MongoConnection uc) {
        return new ConnectionData(uc.getProviderId(),
                uc.getProviderUserId(),
                uc.getDisplayName(),
                uc.getProfileUrl(),
                uc.getImageUrl(),
                decrypt(uc.getAccessToken()),
                decrypt(uc.getSecret()),
                decrypt(uc.getRefreshToken()),
                uc.getExpireTime())
    }

    MongoConnection convert(Connection<?> cnn) {
        ConnectionData data = cnn.createData()

        MongoConnection userConn = new MongoConnection()
        userConn.setProviderId(data.getProviderId())
        userConn.setProviderUserId(data.getProviderUserId())
        userConn.setDisplayName(data.getDisplayName())
        userConn.setProfileUrl(data.getProfileUrl())
        userConn.setImageUrl(data.getImageUrl())
        userConn.setAccessToken(encrypt(data.getAccessToken()))
        userConn.setSecret(encrypt(data.getSecret()))
        userConn.setRefreshToken(encrypt(data.getRefreshToken()))
        userConn.setExpireTime(data.getExpireTime())
        return userConn
    }

    // helper methods

    private String decrypt(String encryptedText) {
        return encryptedText != null ? textEncryptor.decrypt(encryptedText) : encryptedText
    }

    private String encrypt(String text) {
        return text != null ? textEncryptor.encrypt(text) : text
    }
}
