// db-tests/src/test/java/com/example/qa/db/tests/MongoSmokeTest.java
package com.example.qa.db.tests;

import com.mongodb.client.MongoClients;
import io.qameta.allure.*;
import org.bson.Document;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Epic("DB")
@Feature("MongoDB")
public class MongoSmokeTest {

//    @Test
    void mongoInsertAndFind() {
        var uri = System.getProperty("mongo.uri", "mongodb://localhost:27017");
        try (var client = MongoClients.create(uri)) {
            var db = client.getDatabase("app");
            var col = db.getCollection("events");

            col.insertOne(new Document("type", "login").append("user", "qa_user"));

            var found = col.find(new Document("type", "login")).first();
            assertNotNull(found);
            assertEquals("qa_user", found.getString("user"));
        }
    }
}
