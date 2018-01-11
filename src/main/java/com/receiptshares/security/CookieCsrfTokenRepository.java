package com.receiptshares.security;

import org.springframework.http.HttpCookie;
import org.springframework.http.ResponseCookie;
import org.springframework.security.web.server.csrf.CsrfToken;
import org.springframework.security.web.server.csrf.DefaultCsrfToken;
import org.springframework.security.web.server.csrf.ServerCsrfTokenRepository;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.UUID;


public class CookieCsrfTokenRepository implements ServerCsrfTokenRepository{

    private static final String DEFAULT_CSRF_PARAMETER_NAME = "_csrf";
    private static final String DEFAULT_CSRF_HEADER_NAME = "X-CSRF-TOKEN";

    @Override
    public Mono<CsrfToken> generateToken(ServerWebExchange exchange) {
        return Mono.fromCallable(() -> new DefaultCsrfToken(DEFAULT_CSRF_HEADER_NAME, DEFAULT_CSRF_PARAMETER_NAME, UUID.randomUUID().toString()));
    }

    @Override
    public Mono<Void> saveToken(ServerWebExchange exchange, CsrfToken token) {
        exchange.getResponse().addCookie(createCsrfCookie(token));
        return Mono.empty();
    }

    @Override
    public Mono<CsrfToken> loadToken(ServerWebExchange exchange) {
        HttpCookie csrfCookie = exchange.getRequest().getCookies().getFirst(DEFAULT_CSRF_HEADER_NAME);
        return Optional.ofNullable(csrfCookie)
                       .map(HttpCookie::getValue)
                       .map(token -> new DefaultCsrfToken(DEFAULT_CSRF_HEADER_NAME, DEFAULT_CSRF_PARAMETER_NAME, token))
                .<Mono<CsrfToken>>map(Mono::just)
                .orElse(Mono.empty());
    }

    private ResponseCookie createCsrfCookie(CsrfToken token) {
        return ResponseCookie.from(token.getHeaderName(), token.getToken())
                             .httpOnly(false)
                             .secure(true)
                             .build();
    }


}
