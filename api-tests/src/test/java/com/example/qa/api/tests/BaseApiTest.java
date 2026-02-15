// api-tests/src/test/java/com/example/qa/api/tests/BaseApiTest.java
package com.example.qa.api.tests;

import com.example.qa.common.config.AppConfig;
import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.BeforeAll;

import static io.restassured.RestAssured.*;

public abstract class BaseApiTest {
    protected static final AppConfig cfg = ConfigFactory.create(AppConfig.class);

    @BeforeAll
    static void setup() {
        baseURI = cfg.baseUrl();
        basePath = cfg.apiBasePath();
    }
}
