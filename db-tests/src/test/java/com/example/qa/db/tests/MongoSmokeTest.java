// db-tests/src/test/java/com/example/qa/db/tests/MongoSmokeTest.java
package com.example.qa.db.tests;

import com.mongodb.client.MongoClients;
import io.qameta.allure.*;
import org.bson.Document;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Epic("DB")
@Feature("MongoDB")
public class MongoSmokeTest {

    @Test
    void findByType() {
        var uri = System.getProperty("mongo.uri", "mongodb://localhost:27017");
        try (var client = MongoClients.create(uri)) {
            var db = client.getDatabase("app");
            var col = db.getCollection("events");
            var runId = UUID.randomUUID().toString();

            col.insertOne(new Document("type", "login").append("user", "qa_user").append("runId", runId));

            var found = col.find(new Document("type", "login").append("runId", runId)).first();
            assertNotNull(found);
            assertEquals("qa_user", found.getString("user"));
        }
    }

    @Test
    void findByUser() {
        var uri = System.getProperty("mongo.uri", "mongodb://localhost:27017");
        try (var client = MongoClients.create(uri)) {
            var db = client.getDatabase("app");
            var col = db.getCollection("events");
            var runId = UUID.randomUUID().toString();

            col.insertOne(new Document("type", "signup").append("user", "qa_user_2").append("runId", runId));

            var found = col.find(new Document("user", "qa_user_2").append("runId", runId)).first();
            assertNotNull(found);
            assertEquals("signup", found.getString("type"));
        }
    }

    @Test
    void findByTypeAndUser() {
        var uri = System.getProperty("mongo.uri", "mongodb://localhost:27017");
        try (var client = MongoClients.create(uri)) {
            var db = client.getDatabase("app");
            var col = db.getCollection("events");
            var runId = UUID.randomUUID().toString();

            col.insertOne(new Document("type", "purchase").append("user", "qa_user_3").append("runId", runId));

            var found = col.find(new Document("type", "purchase")
                    .append("user", "qa_user_3")
                    .append("runId", runId)).first();
            assertNotNull(found);
        }
    }

    @Test
    void findByNestedField() {
        var uri = System.getProperty("mongo.uri", "mongodb://localhost:27017");
        try (var client = MongoClients.create(uri)) {
            var db = client.getDatabase("app");
            var col = db.getCollection("events");
            var runId = UUID.randomUUID().toString();

            var meta = new Document("device", "mobile").append("os", "android");
            col.insertOne(new Document("type", "session")
                    .append("user", "qa_user_4")
                    .append("meta", meta)
                    .append("runId", runId));

            var found = col.find(new Document("meta.device", "mobile").append("runId", runId)).first();
            assertNotNull(found);
            assertEquals("session", found.getString("type"));
        }
    }

    @Test
    void findByNumericField() {
        var uri = System.getProperty("mongo.uri", "mongodb://localhost:27017");
        try (var client = MongoClients.create(uri)) {
            var db = client.getDatabase("app");
            var col = db.getCollection("events");
            var runId = UUID.randomUUID().toString();

            col.insertOne(new Document("type", "score")
                    .append("user", "qa_user_5")
                    .append("points", 87)
                    .append("runId", runId));

            var found = col.find(new Document("points", 87).append("runId", runId)).first();
            assertNotNull(found);
            assertEquals(87, found.getInteger("points"));
        }
    }
}
