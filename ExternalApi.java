package com.cts.exercises;

/**
 * Represents a third-party / external API that MyService depends on.
 * In a real application this might wrap a REST client, a database call,
 * or any other slow/unpredictable external dependency.
 */
public interface ExternalApi {
    String getData();
}
