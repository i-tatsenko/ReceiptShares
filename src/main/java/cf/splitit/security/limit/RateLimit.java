package cf.splitit.security.limit;

import reactor.core.publisher.Mono;

public interface RateLimit {

    public Mono<Void> checkRate(String key);

}
