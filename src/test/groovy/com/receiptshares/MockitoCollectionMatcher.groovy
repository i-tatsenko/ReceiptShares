package com.receiptshares

import org.mockito.ArgumentMatcher

import static org.mockito.internal.progress.ThreadSafeMockingProgress.mockingProgress

class MockitoCollectionMatcher implements ArgumentMatcher<Collection> {

    final Collection elements

    private MockitoCollectionMatcher(Collection elements) {
        this.elements = elements
    }

    @Override
    boolean matches(Collection argument) {
        return argument.size() == elements.size() && argument.containsAll(elements)
    }

    static <T> List<T> matchesList(Collection<T> list) {
        new MockitoCollectionMatcher(list).reportMatcher()
        return new ArrayList<>()
    }

    static <T> Set<T> matchesSet(Collection<T> set) {
        new MockitoCollectionMatcher(set).reportMatcher()
        return new HashSet<T>()
    }

    private void reportMatcher() {
        mockingProgress().getArgumentMatcherStorage().reportMatcher(this)
    }
}
