package com.cts.exercises;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

/**
 * Mockito Exercise 1: Mocking and Stubbing
 *
 * MyService depends on ExternalApi, which in a real system might call out
 * to a slow or unreliable third-party service. To test MyService in
 * isolation, we replace ExternalApi with a mock and tell that mock exactly
 * what to return, so the test is fast, deterministic, and doesn't touch
 * any real network call.
 */
public class MyServiceTest {

    @Test
    public void testExternalApi() {
        // 1. Create a mock object for the external API
        ExternalApi mockApi = Mockito.mock(ExternalApi.class);

        // 2. Stub the method to return a predefined value
        when(mockApi.getData()).thenReturn("Mock Data");

        // 3. Inject the mock into the service under test
        MyService service = new MyService(mockApi);

        // Act
        String result = service.fetchData();

        // Assert
        assertEquals("Mock Data", result);
    }
}
