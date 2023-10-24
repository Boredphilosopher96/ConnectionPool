package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnectionPool {
    BlockingMPMCQueue<Connection> blockingQueue;

    DatabaseConnectionPool(int maxPoolSize, String dbUrl) {
        this.blockingQueue = new BlockingMPMCQueue<>(maxPoolSize);
        this.initializeConnectionPool(maxPoolSize, dbUrl);
    }

    public static Connection createConnection(String url) throws SQLException {
        return DriverManager.getConnection(url);
    }

    public void initializeConnectionPool(int maxPoolSize, String dbUrl) {
        for (int i = 0; i < maxPoolSize; i++) {
            try {
                this.blockingQueue.offer(createConnection(dbUrl));
            } catch (SQLException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void putConnection(Connection connection) throws PoolException, InterruptedException {
        this.blockingQueue.offer(connection);
    }

    public Connection getConnection() throws SQLException, InterruptedException, PoolException {
        return this.blockingQueue.poll();
    }


}
