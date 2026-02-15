// api-tests/src/test/java/com/example/qa/api/tests/LoginApiTest.java
package com.example.qa.api.tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.*;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@Epic("API")
@Feature("Auth")
public class LoginApiTest extends BaseApiTest {
    private final ObjectMapper om = new ObjectMapper();

    @Test
    @Story("POST /api/login invalid credentials")
    void loginInvalidExample() throws Exception {
        // Під ваш реальний endpoint: /api/login, /auth, /login, ...
        var payload = Map.of("username", "wrong", "password", "wrong");

        given()
                .contentType("application/json")
                .body(om.writeValueAsString(payload))
                .when()
                .post("/api/login")
                .then()
                .statusCode(anyOf(is(400), is(401), is(404)));
    }
}
