package cf.splitit.security.limit;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

public abstract class AbstractRateLimit<T extends LimitCounter> implements RateLimit {

    private final static int DEFAULT_CACHE_SIZE = 1000;
    private final static int DEFAULT_TTL_SECONDS = 10;

    private final int cacheSize;
    private final String name;
    private final Cache<String, T> limitsCache;

    public AbstractRateLimit(String name) {
        this(name, DEFAULT_CACHE_SIZE, DEFAULT_TTL_SECONDS);
    }

    public AbstractRateLimit(String name, int cacheSize, int ttlSeconds) {
        this.name = name;
        this.cacheSize = cacheSize;
        limitsCache = CacheBuilder.<String, AbstractRateLimit>newBuilder()
                .maximumSize(cacheSize)
                .expireAfterAccess(ttlSeconds, TimeUnit.SECONDS)
                .removalListener(removalListener())
                .build();
    }

    @Override
    public Mono<Void> checkRate(String key) {
        return find(key).map(LimitCounter::acquire)
                        .filter(Boolean.TRUE::equals)
                        .switchIfEmpty(Mono.defer(() -> Mono.error(createRateLimitException(key))))
                        .then();
    }

    private RateLimitException createRateLimitException(String key) {
        return retryAfter(key, limitsCache.getIfPresent(key)).map(RateLimitException::new)
                                                             .orElseGet(RateLimitException::new);
    }

    private RemovalListener<String, T> removalListener() {
        return notification -> persistLimit(notification.getKey(), notification.getValue());
    }

    private Mono<T> find(String key) {
        return Optional.ofNullable(limitsCache.getIfPresent(key))
                       .map(Mono::just)
                       .orElseGet(() -> createCounter(key).map(limiter -> cacheCounter(key, limiter)));
    }

    private T cacheCounter(String key, T counter) {
        T found = limitsCache.asMap().putIfAbsent(key, counter);
        return found != null ? found : counter;
    }

    protected Optional<Integer> retryAfter(String key, T counter){
        return Optional.empty();
    }

    protected abstract Mono<T> createCounter(String key);

    protected abstract void persistLimit(String key, T counter);

}
