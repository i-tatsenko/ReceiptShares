package com.receiptshares;

import com.receiptshares.user.dao.RememberMeTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.HttpBasicServerAuthenticationEntryPoint;
import org.springframework.security.web.server.authentication.ServerAuthenticationFailureHandler;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.security.web.server.authentication.logout.ServerLogoutSuccessHandler;
import org.springframework.security.web.server.csrf.WebSessionServerCsrfTokenRepository;
import reactor.core.publisher.Mono;

import javax.servlet.http.Cookie;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfiguration {
    public static final String REMEMBER_ME_COOKIE_NAME = "rembo";
    //TODO: migrate to reactive configuration https://github.com/spring-projects/spring-security/blob/5.0.0.M1/samples/javaconfig/hellowebflux/src/main/java/sample/Application.java
    private MongoTemplate mongoTemplate;

    @Value("${security.rememberme.key}")
    private String rememberMeKey;

    @Autowired
    public SecurityConfiguration(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        return http.formLogin().loginPage("/v1/open/login").authenticationSuccessHandler(authSuccessHandler()).authenticationFailureHandler(authFailureHandler())
                .and()
                .logout().logoutUrl("/v1/open/logout").logoutSuccessHandler(logoutSuccessHandler())
                .and()
                .csrf().csrfTokenRepository(new WebSessionServerCsrfTokenRepository())
                .and()
                .authorizeExchange().pathMatchers("/**").permitAll()
                .pathMatchers("/v1/open/**", "/signin/**").permitAll()
                .pathMatchers("/v1/**").authenticated()
                .and()
                .exceptionHandling().authenticationEntryPoint(new HttpBasicServerAuthenticationEntryPoint())
                .and()
        .build();
    }

    protected void configure(HttpSecurity http) throws Exception {
//        http.rememberMe().rememberMeServices(tokenBasedRememberMeServices())
//            .and()
//            .formLogin().loginProcessingUrl("/v1/open/login").successHandler(authSuccessHandler()).failureHandler(authFailureHandler())
//            .and()
//            .logout().logoutUrl("/v1/open/logout").deleteCookies(REMEMBER_ME_COOKIE_NAME).logoutSuccessHandler(logoutSuccessHandler())
//            .and()
//            .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
//            .and()
//            .antMatcher("/**").authorizeRequests()
//            .antMatchers("/v1/open/**", "/signin/**").permitAll()
//            .antMatchers("/v1/**").authenticated()
//            .anyRequest().permitAll()
//            .and()
//            .exceptionHandling().authenticationEntryPoint(new Http403ForbiddenEntryPoint());
    }


//    @Bean
//    public RememberMeTokenRepository rememberMeTokenRepository() {
//        return new RememberMeTokenRepository(mongoTemplate);
//    }

//    @Bean
//    public RememberMeServices tokenBasedRememberMeServices() {
//        PersistentTokenBasedRememberMeServices result = new PersistentTokenBasedRememberMeServices(rememberMeKey, userDetailsService(), rememberMeTokenRepository());
//        result.setAlwaysRemember(true);
//        result.setCookieName(REMEMBER_ME_COOKIE_NAME);
//        result.setTokenValiditySeconds(7 * 24 * 60 * 60);
//        return result;
//    }

    @Bean
    public ServerLogoutSuccessHandler logoutSuccessHandler() {
        return (webFilterExchange, authentication) -> Mono.just(webFilterExchange.getExchange().getResponse().setStatusCode(HttpStatus.OK)).then();
    }

    @Bean
    protected ServerAuthenticationSuccessHandler authSuccessHandler() {
        return (webFilterExchange, authentication) -> Mono.just(webFilterExchange.getExchange().getResponse().setStatusCode(HttpStatus.OK)).then();
    }

    @Bean
    protected ServerAuthenticationFailureHandler authFailureHandler() {
        return (webFilterExchange, exception) -> {
            webFilterExchange.getExchange().getResponse().addCookie(ResponseCookie.from(REMEMBER_ME_COOKIE_NAME, "").build());
            return Mono.just(webFilterExchange.getExchange().getResponse().setStatusCode(HttpStatus.UNAUTHORIZED)).then();
        };
    }
}
