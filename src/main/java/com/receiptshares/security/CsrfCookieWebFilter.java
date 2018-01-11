package com.receiptshares.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.security.web.server.csrf.CsrfToken;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

public class CsrfCookieWebFilter implements WebFilter, Ordered {

    private static final Logger LOGGER = LoggerFactory.getLogger(CsrfCookieWebFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        Mono<CsrfToken> token = exchange.getAttribute(CsrfToken.class.getName());
        if (token == null) {
            token = Mono.empty();
        }
        return token.doOnNext(t -> LOGGER.trace("Requesting token to save it in cookies"))
                    .then(chain.filter(exchange));

    }

    @Override
    public int getOrder() {
        return LOWEST_PRECEDENCE;
    }
}
