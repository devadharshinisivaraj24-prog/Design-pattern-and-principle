package com.cts.tdd.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@DisplayName("Exercise 2: Verifying Interactions")
class MyServiceTest {

    private ExternalApi mockApi;
    private MyService service;

    @BeforeEach
    void setUp() {
        mockApi = Mockito.mock(ExternalApi.class);
        service = new MyService(mockApi);
    }

    @Test
    @DisplayName("Should verify that getData() was called exactly once")
    void testVerifyInteraction() {
        when(mockApi.getData()).thenReturn("mock-data");

        String result = service.fetchData();

        assertEquals("mock-data", result);

        verify(mockApi).getData();
        verify(mockApi, times(1)).getData();
    }

    @Test
    @DisplayName("Should verify getDataByQuery() was called with the exact argument")
    void testVerifyInteractionWithSpecificArgument() {
        when(mockApi.getDataByQuery("employee-101")).thenReturn("John Doe");

        String result = service.fetchDataFor("employee-101");

        assertEquals("John Doe", result);

        verify(mockApi).getDataByQuery("employee-101");
        verify(mockApi, never()).getDataByQuery("employee-999");
    }

    @Test
    @DisplayName("Should capture and assert on the argument passed to the mock")
    void testVerifyInteractionUsingArgumentCaptor() {
        when(mockApi.getDataByQuery(anyString())).thenReturn("captured-data");
        ArgumentCaptor<String> queryCaptor = ArgumentCaptor.forClass(String.class);

        service.fetchDataFor("employee-202");

        verify(mockApi).getDataByQuery(queryCaptor.capture());

        assertEquals("employee-202", queryCaptor.getValue());
    }

    @Test
    @DisplayName("Should verify zero interactions when service method is not called")
    void testNoInteractionsWhenMethodNotCalled() {
        verify(mockApi, never()).getData();
        verify(mockApi, never()).getDataByQuery(anyString());
    }
}
