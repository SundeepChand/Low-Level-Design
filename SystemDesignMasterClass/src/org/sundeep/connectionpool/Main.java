package org.sundeep.connectionpool;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

class PooledConnectionHandler implements InvocationHandler {
    private final Connection connection;
    private final CustomConnectionPool pool;

    public PooledConnectionHandler(Connection connection, CustomConnectionPool pool) {
        this.connection = connection;
        this.pool = pool;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.equals(Connection.class.getMethod("close"))) {
            pool.releaseConnection(connection);
            return null;
        }
        return method.invoke(connection, args);
    }
}

class CustomConnectionPool {
    private final BlockingQueue<Connection> connectionPool;

    private final int poolSize;

    public CustomConnectionPool(int poolSize, String url, String user, String password) throws SQLException {
        this.poolSize = poolSize;
        this.connectionPool = new ArrayBlockingQueue<>(this.poolSize);

        for (int i = 0; i < this.poolSize; i++) {
            Connection conn = DriverManager.getConnection(url, user, password);
            connectionPool.offer(conn);
        }
    }

    public Connection getConnection() throws InterruptedException {
        Connection conn = connectionPool.take();

        return (Connection) Proxy.newProxyInstance(
            Connection.class.getClassLoader(),
            new Class<?>[]{Connection.class},
            (proxy, method, args) -> {
                if (method.getName().equals("close")) {
                    this.releaseConnection(conn);
                    return null;
                }
                return method.invoke(conn, args);
            }
        );
    }

    public void releaseConnection(Connection conn) {
        if (conn != null) {
            connectionPool.offer(conn);
        }
    }

    public void shutdown() throws SQLException {
        for (Connection conn: connectionPool) {
            conn.close();
        }
    }
}

public class Main {
    public static void main(String[] args) {
        long start = System.nanoTime();

//        noConnectionPool();
        withConnectionPool();

        long end = System.nanoTime();
        long durationNs = end - start;

        System.out.println("Time taken: " + durationNs + " ns");
        System.out.println("Time taken: " + durationNs / 1_000_000.0 + " ms");
    }

    private static void noConnectionPool() {
        // This method simulates the scenario without using a connection pool.
        // Each time a connection is needed, a new one is created and closed after use.
        // This can lead to performance issues due to the overhead of establishing connections.

        String url = "jdbc:mysql://localhost:3306/mydb?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
        String user = "root";
        String password = "my-secret-pw";

        int numThreads = 151; // Default max allowed connections in MySQL is 151, so we can test the limit without connection pool.

        String sql = "SELECT * FROM users WHERE id = ?";

        List<Thread> threads = new ArrayList<>();

        for (int i = 0; i < numThreads; i++) {
            Thread t = new Thread(() -> {
                try (Connection conn = DriverManager.getConnection(url, user, password)) {

                    PreparedStatement ps = conn.prepareStatement(sql);
                    ps.setInt(1, 1);

                    try (ResultSet rs = ps.executeQuery()) {
                        while (rs.next()) {
                            System.out.println("User ID: " + rs.getInt("id"));
                            System.out.println("Name: " + rs.getString("name"));
                            System.out.println("Age: " + rs.getInt("age"));
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            threads.add(t);
            t.start();
        }

        for (Thread t: threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static void withConnectionPool() {
        String url = "jdbc:mysql://localhost:3306/mydb?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
        String user = "root";
        String password = "my-secret-pw";

        int numThreads = 1000;

        String sql = "SELECT * FROM users WHERE id = ?";

        final CustomConnectionPool pool;
        try {
            pool = new CustomConnectionPool(6, url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }
        System.out.println("Connection pool created with 10 connections.");

        List<Thread> threads = new ArrayList<>();

        for (int i = 0; i < numThreads; i++) {
            Thread t = new Thread(() -> {
                try (Connection conn = pool.getConnection()) {

                    PreparedStatement ps = conn.prepareStatement(sql);
                    ps.setInt(1, 1);

                    try (ResultSet rs = ps.executeQuery()) {
                        while (rs.next()) {
                            System.out.println("User ID: " + rs.getInt("id"));
                            System.out.println("Name: " + rs.getString("name"));
                            System.out.println("Age: " + rs.getInt("age"));
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            threads.add(t);
            t.start();
        }

        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        try {
            pool.shutdown();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
