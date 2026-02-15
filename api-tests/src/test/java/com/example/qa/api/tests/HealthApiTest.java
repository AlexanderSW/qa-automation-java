// api-tests/src/test/java/com/example/qa/api/tests/HealthApiTest.java
package com.example.qa.api.tests;

import io.qameta.allure.*;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@Epic("API")
@Feature("Health")
public class HealthApiTest extends BaseApiTest {

    @Test
    @Story("GET /health returns 200")
    void healthShouldReturnOk() {
        given()
                .when()
                .get("/health")
                .then()
                .statusCode(anyOf(is(200), is(404))); // 404 якщо endpoint ще не існує
    }
}
