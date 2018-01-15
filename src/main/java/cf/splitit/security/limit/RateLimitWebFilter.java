package cf.splitit.security.limit;

import cf.splitit.user.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.net.InetSocketAddress;
import java.security.Principal;

@Slf4j
public class RateLimitWebFilter implements WebFilter {

    private final RateLimit rateLimit;

    public RateLimitWebFilter(RateLimit rateLimit) {
        this.rateLimit = rateLimit;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        return findRemoteId(exchange).flatMap(rateLimit::checkRate)
                                     .then(chain.filter(exchange))
                                     .onErrorResume(RateLimitException.class, e -> handleRateLimitException(exchange));
    }

    private Mono<String> findRemoteId(ServerWebExchange exchange) {
        return exchange.getPrincipal().flatMap(this::mapPrincipal)
                       .switchIfEmpty(Mono.fromCallable(() -> getRemoteIpAsString(exchange.getRequest())));
    }

    private Mono<String> mapPrincipal(Principal principal) {
        if (principal instanceof User) {
            return Mono.just((User) principal)
                       .map(u -> u.getPerson().getId());
        }
        return Mono.empty();
    }

    private Mono<Void> handleRateLimitException(ServerWebExchange exchange) {
        findRemoteId(exchange).subscribe(id -> log.warn("Too many requests from " + id));

        exchange.getResponse().setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
        return exchange.getResponse().setComplete();
    }

    private String getRemoteIpAsString(ServerHttpRequest request) {
        InetSocketAddress remoteAddress = request.getRemoteAddress();
        if (remoteAddress == null) {
            log.warn("Can't determine remote address for " + request);
            return "N\\A";
        }
        return remoteAddress.getAddress().getHostAddress();
    }
}
