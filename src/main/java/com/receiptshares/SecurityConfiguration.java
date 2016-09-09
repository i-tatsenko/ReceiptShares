package com.receiptshares;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
            .and()
            .antMatcher("/v1/open/**").authorizeRequests().anyRequest().permitAll()
            .and()
            .antMatcher("/v1/**").authorizeRequests().anyRequest().authenticated()
            .and()
            .antMatcher("/**").authorizeRequests().anyRequest().permitAll();
    }
}
