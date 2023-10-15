package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnectionPool {
    BlockingMPMCQueue<Connection> blockingQueue;
    private final String dbUrl;

    DatabaseConnectionPool(int maxPoolSize, String dbUrl) {
        this.blockingQueue = new BlockingMPMCQueue<>(maxPoolSize);
        this.dbUrl = dbUrl;
    }


    public void putConnection(Connection connection) throws PoolException {
        this.blockingQueue.offer(connection);
    }

    public static Connection createConnection(String url) throws SQLException {
        return DriverManager.getConnection(url);
    }

    public Connection getConnection() throws SQLException {
        try {
            return this.blockingQueue.poll();
        } catch (PoolException e) {
            return createConnection(this.dbUrl);
        }
    }

    public int getMaxPoolSize() {
        return this.blockingQueue.getSize();
    }

}
