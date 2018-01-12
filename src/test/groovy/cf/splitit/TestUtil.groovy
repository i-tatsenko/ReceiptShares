package cf.splitit

import org.springframework.web.context.request.async.DeferredResult

import java.time.Duration
import java.util.concurrent.TimeoutException

import static java.lang.System.nanoTime

class TestUtil {

    static def DEFAULT_TIMEOUT = 5_000

    static def waitForResult(DeferredResult res) {
        waitForResult(res, DEFAULT_TIMEOUT)
    }

    static def waitForResult(DeferredResult res, long timeoutInMs) {
        def start = nanoTime()
        while (!res.hasResult()) {
            if (!Duration.ofNanos(nanoTime() - start).minusMillis(timeoutInMs).isNegative()) {
                throw new TimeoutException("Timeout after ${timeoutInMs} ms of waiting")
            }
            Thread.sleep(10)
        }


    }
}