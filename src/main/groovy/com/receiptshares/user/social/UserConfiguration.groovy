package com.receiptshares.user.social

import com.receiptshares.user.dao.UserService
import com.receiptshares.user.social.mongo.ConnectionConverter
import com.receiptshares.user.social.mongo.MongoConnectionService
import com.receiptshares.user.social.mongo.MongoUsersConnectionRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.encrypt.Encryptors
import org.springframework.social.UserIdSource
import org.springframework.social.config.annotation.ConnectionFactoryConfigurer
import org.springframework.social.config.annotation.EnableSocial
import org.springframework.social.config.annotation.SocialConfigurer
import org.springframework.social.connect.ConnectionFactoryLocator
import org.springframework.social.connect.UsersConnectionRepository
import org.springframework.social.connect.web.ProviderSignInController
import org.springframework.social.facebook.connect.FacebookConnectionFactory


@Configuration
@EnableSocial
class UserConfiguration implements SocialConfigurer {

    private UserService userService
    private MongoTemplate mongoTemplate

    @Autowired
    UserConfiguration(UserService userService, MongoTemplate mongoTemplate) {
        this.userService = userService
        this.mongoTemplate = mongoTemplate
    }

    @Bean
    ProviderSignInController providerSignInController(ConnectionFactoryLocator locator,
                                                      UsersConnectionRepository repo,
                                                      OAuthSingInAdapter singInAdapter) {
        return new ProviderSignInController(locator, repo, singInAdapter)
    }

    @Override
    void addConnectionFactories(ConnectionFactoryConfigurer connectionFactoryConfigurer, Environment env) {
        FacebookConnectionFactory fb = new FacebookConnectionFactory(env.getProperty("fb.app.id"), env.getProperty("fb.app.secret"))
        fb.setScope("public_profile,email,user_friends")
        connectionFactoryConfigurer.addConnectionFactory(fb)
    }

    @Override
    UserIdSource getUserIdSource() {
        return {SecurityContextHolder.context.authentication.principal.email}
    }

    /**
     * Shchema location
     * spring-social-core-1.1.4.RELEASE.jar!/org/springframework/social/connect/jdbc/JdbcUsersConnectionRepository.sql
     */
    @Override
    UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {
        def encryptor = Encryptors.noOpText()
        def service = new MongoConnectionService(mongoTemplate, new ConnectionConverter(connectionFactoryLocator, encryptor))
        UsersConnectionRepository repository = new MongoUsersConnectionRepository(service, connectionFactoryLocator, encryptor)
        repository.setConnectionSignUp(new OAuthImplicitRegistration(userService))
        return repository
    }
}
