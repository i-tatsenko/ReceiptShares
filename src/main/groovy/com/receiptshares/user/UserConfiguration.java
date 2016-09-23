package com.receiptshares.user;

import com.receiptshares.user.dao.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.social.UserIdSource;
import org.springframework.social.config.annotation.ConnectionFactoryConfigurer;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.config.annotation.SocialConfigurer;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository;
import org.springframework.social.connect.web.ProviderSignInController;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;

import javax.sql.DataSource;

@Configuration
@EnableSocial
public class UserConfiguration implements SocialConfigurer {

    private DataSource dataSource;
    private UserService userService;

    @Autowired
    public UserConfiguration(DataSource dataSource, UserService userService) {
        this.dataSource = dataSource;
        this.userService = userService;
    }

    @Bean
    public ProviderSignInController providerSignInController(ConnectionFactoryLocator locator,
                                                             UsersConnectionRepository repo,
                                                             OAuthSingInAdapter singInAdapter) {
        return new ProviderSignInController(locator, repo, singInAdapter);
    }

    @Override
    public void addConnectionFactories(ConnectionFactoryConfigurer connectionFactoryConfigurer, Environment env) {
        FacebookConnectionFactory fb = new FacebookConnectionFactory(env.getProperty("fb.app.id"), env.getProperty("fb.app.secret"));
        fb.setScope("public_profile,email");
        connectionFactoryConfigurer.addConnectionFactory(fb);
    }

    @Override
    public UserIdSource getUserIdSource() {
        return null;
    }

    /**
     * Shchema location
     * spring-social-core-1.1.4.RELEASE.jar!/org/springframework/social/connect/jdbc/JdbcUsersConnectionRepository.sql
     */
    @Override
    public UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {
        JdbcUsersConnectionRepository repository = new JdbcUsersConnectionRepository(dataSource, connectionFactoryLocator, Encryptors.noOpText());
        repository.setConnectionSignUp(new OAuthImplicitRegistration(userService));
        return repository;
    }
}
