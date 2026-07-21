package com.cts.exercises;

/**
 * A service that depends on ExternalApi to fetch data.
 * The dependency is injected via the constructor, which is what makes it
 * possible to substitute a mock ExternalApi during testing.
 */
public class MyService {

    private final ExternalApi externalApi;

    public MyService(ExternalApi externalApi) {
        this.externalApi = externalApi;
    }

    public String fetchData() {
        return externalApi.getData();
    }
}
