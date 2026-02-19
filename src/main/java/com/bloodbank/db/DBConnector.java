package com.bloodbank.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnector {

    private static final String DB_URL = "jdbc:sqlite:data/bloodbank.db";

    public static Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(DB_URL);
        configureSQLite(conn);
        return conn;
    }

    private static void configureSQLite(Connection conn) {
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("PRAGMA foreign_keys = ON");
            stmt.execute("PRAGMA journal_mode = WAL");
            stmt.execute("PRAGMA synchronous = NORMAL");
            stmt.execute("PRAGMA busy_timeout = 10000");
        } catch (SQLException e) {
            System.err.println("Warning: Could not configure SQLite: " + e.getMessage());
        }
    }

}
