package cf.splitit

import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.TestInstancePostProcessor
import org.mockito.MockitoAnnotations

class MockitoExtension implements TestInstancePostProcessor {

    @Override
    void postProcessTestInstance(Object testInstance, ExtensionContext context) throws Exception {
        MockitoAnnotations.initMocks(testInstance)
    }
}
