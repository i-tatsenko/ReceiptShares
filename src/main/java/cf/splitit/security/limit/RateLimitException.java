package cf.splitit.security.limit;

import java.util.Optional;

public class RateLimitException extends Exception {

    private final int retryAfter;

    public RateLimitException() {
        retryAfter = -1;
    }

    public RateLimitException(int retryAfter) {
        this.retryAfter = retryAfter;
    }

    public Optional<Integer> retryAfter() {
        return Optional.of(retryAfter)
                       .filter(after -> after > 0);
    }

}
