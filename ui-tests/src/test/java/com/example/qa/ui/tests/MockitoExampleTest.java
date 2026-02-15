// ui-tests/src/test/java/com/example/qa/ui/tests/MockitoExampleTest.java
package com.example.qa.ui.tests;

import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class MockitoExampleTest {
    interface TokenService { String token(); }

    @Test
    void mockitoWorks() {
        var svc = mock(TokenService.class);
        when(svc.token()).thenReturn("abc");
        assertEquals("abc", svc.token());
        verify(svc, times(1)).token();
    }
}
