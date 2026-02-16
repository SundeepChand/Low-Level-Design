package org.sundeep.mysql.readreplica;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

class DataSourceFactory {
    public static DataSource createDataSource(String url, String user, String password, int maxPoolSize) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setUsername(user);
        config.setPassword(password);

        config.setMaximumPoolSize(maxPoolSize);
        config.setMinimumIdle(5);
        config.setConnectionTimeout(30000);
        config.setIdleTimeout(600000);
        config.setMaxLifetime(1800000);

        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.addDataSourceProperty("useServerPrepStmts", "true");

        return new HikariDataSource(config);
    }
}

class DatabasePools {

    public static final DataSource WRITE_DS =
            DataSourceFactory.createDataSource(
                    "jdbc:mysql://localhost:3306/mydb",
                    "root",
                    "pwd",
                    20
            );

    public static final DataSource READ_DS =
            DataSourceFactory.createDataSource(
                    "jdbc:mysql://localhost:3307/mydb",
                    "root",
                    "pwd",
                    20
            );
}

class RoutingDataSource {
    public static Connection getConnection(boolean isWrite) throws Exception {
        if (isWrite) {
            return DatabasePools.WRITE_DS.getConnection();
        } else {
            return DatabasePools.READ_DS.getConnection();
        }
    }
}

public class Main {
    public static void main(String[] args) {
        long savedId = 1;
        try (Connection conn = RoutingDataSource.getConnection(true)) {
            var stmt = conn.prepareStatement(
                "INSERT INTO users(name, age) VALUES (?, ?)",
                Statement.RETURN_GENERATED_KEYS
            );
            stmt.setString(1, "Sundeep");
            stmt.setInt(2, 24);
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    savedId = rs.getLong(1);
                    System.out.println("Saved ID: " + savedId);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try (Connection conn = RoutingDataSource.getConnection(false)) {
            var stmt = conn.prepareStatement(
                    "SELECT * FROM users WHERE id = ?"
            );
            stmt.setLong(1, savedId);

            var rs = stmt.executeQuery();
            while (rs.next()) {
                System.out.println("User ID: " + rs.getInt("id"));
                System.out.println("Name: " + rs.getString("name"));
                System.out.println("Age: " + rs.getInt("age"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
