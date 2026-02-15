// db-tests/src/test/java/com/example/qa/db/tests/JdbcSmokeTest.java
package com.example.qa.db.tests;

import com.example.qa.db.jdbc.JdbcPool;
import io.qameta.allure.*;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

@Epic("DB")
@Feature("JDBC")
public class JdbcSmokeTest {

//    @Test
    @Story("Postgres: create table + insert + select")
    void postgresExample() throws Exception {
        try (Connection c = JdbcPool.postgres().getConnection()) {
            c.createStatement().execute("""
        create table if not exists users(
          id serial primary key,
          username varchar(50) not null unique
        )
      """);

            // insert ignore-like
            c.createStatement().execute("""
        insert into users(username)
        values ('qa_user')
        on conflict (username) do nothing
      """);

            var rs = c.createStatement().executeQuery("select count(*) from users");
            assertTrue(rs.next());
            assertTrue(rs.getInt(1) >= 1);
        }
    }

    @Test
    @Story("MySQL: select version")
    void mysqlExample() throws Exception {
        try (Connection c = JdbcPool.mysql().getConnection()) {
            var rs = c.createStatement().executeQuery("select version()");
            System.out.println("version: " + rs);
            assertTrue(rs.next());
            assertNotNull(rs.getString(1));
        }
    }

    @Test
    @Story("MySQL: create test table in mini_crud")
    void createTestTable() throws Exception {
        try (Connection c = JdbcPool.mysql().getConnection();
             Statement st = c.createStatement()) {
            st.execute("""
                create table if not exists test (
                  id int auto_increment primary key,
                  name varchar(100) not null,
                  created_at timestamp default current_timestamp
                )
                """);

            var rs = st.executeQuery("""
                select count(*)
                from information_schema.tables
                where table_schema = 'mini_crud'
                  and table_name = 'test'
                """);
            assertTrue(rs.next());
            assertEquals(1, rs.getInt(1));
        }
    }

    @Test
    @Story("MySQL: insert user into users")
    void insertUserIntoUsersTable() throws Exception {
        var login = "qa_user_" + System.currentTimeMillis();
        var passwordHash = "$2y$10$qaAutomationDummyHash";

        try (Connection c = JdbcPool.mysql().getConnection()) {
            var insert = c.prepareStatement("""
                insert into users(login, password_hash)
                values (?, ?)
                """);
            insert.setString(1, login);
            insert.setString(2, passwordHash);
            var updatedRows = insert.executeUpdate();
            assertEquals(1, updatedRows);

            var select = c.prepareStatement("""
                select count(*)
                from users
                where login = ? and password_hash = ?
                """);
            select.setString(1, login);
            select.setString(2, passwordHash);
            var rs = select.executeQuery();
            assertTrue(rs.next());
            assertEquals(1, rs.getInt(1));
        }
    }
}
