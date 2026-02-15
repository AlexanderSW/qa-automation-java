// common/src/test/java/com/example/qa/common/builders/CredentialsTest.java
package com.example.qa.common.builders;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CredentialsTest {
    @Test
    void builderBuildsCredentials() {
        var c = Credentials.builder().username("u").password("p").build();
        assertEquals("u", c.username());
        assertEquals("p", c.password());
    }
}
