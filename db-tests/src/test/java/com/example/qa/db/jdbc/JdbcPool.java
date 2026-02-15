// db-tests/src/test/java/com/example/qa/db/jdbc/JdbcPool.java
package com.example.qa.db.jdbc;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

public class JdbcPool {

    public static DataSource postgres() {
        var cfg = new HikariConfig();
        cfg.setJdbcUrl(System.getProperty("pg.url", "jdbc:postgresql://localhost:5432/app"));
        cfg.setUsername(System.getProperty("pg.user", "user"));
        cfg.setPassword(System.getProperty("pg.pass", "pass"));
        return new HikariDataSource(cfg);
    }

    public static DataSource mysql() {
        var cfg = new HikariConfig();
        cfg.setJdbcUrl(System.getProperty("mysql.url", "jdbc:mysql://localhost:3306/mini_crud"));
        cfg.setUsername(System.getProperty("mysql.user", "root"));
        cfg.setPassword(System.getProperty("mysql.pass", ""));
        return new HikariDataSource(cfg);
    }
}
