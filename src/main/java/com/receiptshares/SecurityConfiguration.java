package com.receiptshares;

import com.receiptshares.user.dao.RememberMeTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.Http401AuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    public static final String REMEMBER_ME_COOKIE_NAME = "rembo";
    //TODO: migrate to reactive configuration https://github.com/spring-projects/spring-security/blob/5.0.0.M1/samples/javaconfig/hellowebflux/src/main/java/sample/Application.java
    private final UserDetailsService userDetailsService;
    private MongoTemplate mongoTemplate;

    @Value("${security.rememberme.key}")
    private String rememberMeKey;

    @Autowired
    public SecurityConfiguration(UserDetailsService userDetailsService, MongoTemplate mongoTemplate) {
        this.userDetailsService = userDetailsService;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.rememberMe().rememberMeServices(tokenBasedRememberMeServices())
            .and()
            .formLogin().loginProcessingUrl("/v1/open/login").successHandler(authSuccessHandler()).failureHandler(authFailureHandler())
            .and()
            .logout().logoutUrl("/v1/open/logout").deleteCookies(REMEMBER_ME_COOKIE_NAME).logoutSuccessHandler(logoutSuccessHandler())
            .and()
            .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
            .and()
            .antMatcher("/**").authorizeRequests()
            .antMatchers("/v1/open/**", "/signin/**").permitAll()
            .antMatchers("/v1/**").authenticated()
            .anyRequest().permitAll()
            .and()
            .exceptionHandling().authenticationEntryPoint(new Http401AuthenticationEntryPoint(""));
    }

    @Override
    protected UserDetailsService userDetailsService() {
        return userDetailsService;
    }

    @Bean
    public RememberMeTokenRepository rememberMeTokenRepository() {
        return new RememberMeTokenRepository(mongoTemplate);
    }

    @Bean
    public RememberMeServices tokenBasedRememberMeServices() {
        PersistentTokenBasedRememberMeServices result = new PersistentTokenBasedRememberMeServices(rememberMeKey, userDetailsService(), rememberMeTokenRepository());
        result.setAlwaysRemember(true);
        result.setCookieName(REMEMBER_ME_COOKIE_NAME);
        result.setTokenValiditySeconds(7 * 24 * 60 * 60);
        return result;
    }

    @Bean
    public LogoutSuccessHandler logoutSuccessHandler() {
        return (request, response, auth) -> response.setStatus(HttpStatus.OK.value());
    }

    @Bean
    protected AuthenticationSuccessHandler authSuccessHandler() {
        return (request, response, auth) -> response.setStatus(HttpStatus.OK.value());
    }

    @Bean
    protected AuthenticationFailureHandler authFailureHandler() {
        return (request, response, auth) -> response.setStatus(HttpStatus.UNAUTHORIZED.value());
    }
}
