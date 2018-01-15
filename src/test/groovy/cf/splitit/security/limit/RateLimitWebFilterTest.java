package cf.splitit.security.limit;

import cf.splitit.MockitoExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;

@ExtendWith(MockitoExtension.class)
class RateLimitWebFilterTest {

    @Mock
    private RateLimit rateLimit;

    @InjectMocks
    private RateLimitWebFilter underTest;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private ServerWebExchange serverWebExchange;
    @Mock
    private WebFilterChain webFilterChain;

    @Test
    void shouldProceedWhenRateLimitIsOk() {

    }

}