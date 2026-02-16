// db-tests/src/test/java/com/example/qa/db/tests/JdbcSmokeTest.java
package com.example.qa.db.tests;

import com.example.qa.db.jdbc.JdbcPool;
import io.qameta.allure.*;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @Test
    @Story("MySQL: print users with login starting qa_user")
    void printQaUsers() throws Exception {
        try (Connection c = JdbcPool.mysql().getConnection()) {
            var select = c.prepareStatement("""
                select id, login
                from users
                where login like 'qa_user%'
                order by id
                """);
            var rs = select.executeQuery();
            while (rs.next()) {
                var id = rs.getInt("id");
                var login = rs.getString("login");
                System.out.println("qa_user: id=" + id + ", login=" + login);
            }
        }
    }

    @Test
    @Story("MySQL: join users and user_tokens by user_id")
    void joinUsersWithTokens() throws Exception {
        try (Connection c = JdbcPool.mysql().getConnection()) {
            var select = c.prepareStatement("""
                select u.id as user_id, u.login, ut.id as token_id
                from users u
                join user_tokens ut on ut.user_id = u.id
                limit 1
                """);
            var rs = select.executeQuery();
            assertTrue(rs.next());
            var userId = rs.getInt("user_id");
            var login = rs.getString("login");
            var tokenId = rs.getInt("token_id");
            System.out.println("join: user_id=" + userId + ", login=" + login + ", token_id=" + tokenId);
            assertTrue(userId > 0);
            assertNotNull(login);
            assertTrue(tokenId > 0);
        }
    }

    @Test
    @Story("MySQL: left join users and user_tokens by user_id")
    void leftJoinUsersWithTokens() throws Exception {
        try (Connection c = JdbcPool.mysql().getConnection()) {
            var select = c.prepareStatement("""
                select u.id as user_id, u.login, ut.id as token_id
                from users u
                left join user_tokens ut on ut.user_id = u.id
                limit 1
                """);
            var rs = select.executeQuery();
            assertTrue(rs.next());
            var userId = rs.getInt("user_id");
            var login = rs.getString("login");
            var tokenId = rs.getObject("token_id");
            System.out.println("left join: user_id=" + userId + ", login=" + login + ", token_id=" + tokenId);
            assertTrue(userId > 0);
            assertNotNull(login);
        }
    }

    @Test
    @Story("MySQL: Stream API filter + map for users")
    void streamFilterAndMapUsers() throws Exception {
        try (Connection c = JdbcPool.mysql().getConnection()) {
            var prefix = "stream_user_" + System.currentTimeMillis() + "_";
            insertStreamUsers(c, prefix, 3);

            var logins = selectLoginsByPrefix(c, prefix);
            var upper = logins.stream()
                    .filter(l -> l.startsWith("stream_user_"))
                    .map(String::toUpperCase)
                    .toList();
            System.out.println("stream filter+map: " + upper);

            assertEquals(3, upper.size());
            assertTrue(upper.stream().allMatch(l -> l.startsWith("STREAM_USER_")));
        }
    }

    @Test
    @Story("MySQL: Stream API distinct + sorted for user ids")
    void streamDistinctAndSortedUserIds() throws Exception {
        try (Connection c = JdbcPool.mysql().getConnection()) {
            var prefix = "stream_user_" + System.currentTimeMillis() + "_";
            insertStreamUsers(c, prefix, 4);

            var ids = selectUserIdsByPrefix(c, prefix);
            var distinctSorted = ids.stream().distinct().sorted().toList();
            var sorted = ids.stream().sorted().toList();
            System.out.println("stream distinct+sorted: " + distinctSorted);

            assertEquals(ids.size(), distinctSorted.size());
            assertEquals(sorted, distinctSorted);
        }
    }

    @Test
    @Story("MySQL: Stream API partitioning for users")
    void streamPartitionUsers() throws Exception {
        try (Connection c = JdbcPool.mysql().getConnection()) {
            var prefix = "stream_user_" + System.currentTimeMillis() + "_";
            insertStreamUsers(c, prefix, 2);

            var logins = selectLoginsByPrefix(c, prefix);
            Map<Boolean, List<String>> partitioned = logins.stream()
                    .collect(Collectors.partitioningBy(l -> l.contains("stream_user_")));
            System.out.println("stream partition: " + partitioned);

            assertEquals(logins.size(), partitioned.get(true).size());
        }
    }

    private void insertStreamUsers(Connection c, String prefix, int count) throws Exception {
        var insert = c.prepareStatement("""
                insert into users(login, password_hash)
                values (?, ?)
                """);
        var passwordHash = "$2y$10$qaAutomationDummyHash";
        for (int i = 0; i < count; i++) {
            insert.setString(1, prefix + i);
            insert.setString(2, passwordHash);
            insert.executeUpdate();
        }
    }

    private List<String> selectLoginsByPrefix(Connection c, String prefix) throws Exception {
        var select = c.prepareStatement("""
                select login
                from users
                where login like ?
                order by id
                """);
        select.setString(1, prefix + "%");
        var rs = select.executeQuery();
        var logins = new ArrayList<String>();
        while (rs.next()) {
            logins.add(rs.getString("login"));
        }
        return logins;
    }

    private List<Integer> selectUserIdsByPrefix(Connection c, String prefix) throws Exception {
        var select = c.prepareStatement("""
                select id
                from users
                where login like ?
                order by id
                """);
        select.setString(1, prefix + "%");
        var rs = select.executeQuery();
        var ids = new ArrayList<Integer>();
        while (rs.next()) {
            ids.add(rs.getInt("id"));
        }
        return ids;
    }
}
