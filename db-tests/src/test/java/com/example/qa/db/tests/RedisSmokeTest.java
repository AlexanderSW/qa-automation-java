// db-tests/src/test/java/com/example/qa/db/tests/RedisSmokeTest.java
package com.example.qa.db.tests;

import io.qameta.allure.*;
import org.junit.jupiter.api.Test;
import redis.clients.jedis.Jedis;

import static org.junit.jupiter.api.Assertions.*;

@Epic("DB")
@Feature("Redis")
public class RedisSmokeTest {

//    @Test
    void redisSetGet() {
        var host = System.getProperty("redis.host", "localhost");
        var port = Integer.parseInt(System.getProperty("redis.port", "6379"));

        try (var jedis = new Jedis(host, port)) {
            jedis.set("qa:key", "ok");
            assertEquals("ok", jedis.get("qa:key"));
        }
    }
}
