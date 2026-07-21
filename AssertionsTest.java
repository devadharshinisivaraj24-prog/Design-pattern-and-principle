package com.cts.exercises;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Exercise 3: Assertions in JUnit
 *
 * Demonstrates the core JUnit 5 assertion methods:
 * assertEquals, assertTrue, assertFalse, assertNull, assertNotNull.
 */
public class AssertionsTest {

    @Test
    public void testAssertions() {
        // Assert equals: checks 2 + 3 evaluates to 5
        assertEquals(5, 2 + 3);

        // Assert true: checks the condition holds
        assertTrue(5 > 3);

        // Assert false: checks the condition does NOT hold
        assertFalse(5 < 3);

        // Assert null: checks the reference is null
        assertNull(null);

        // Assert not null: checks a newly created object is not null
        assertNotNull(new Object());
    }
}
