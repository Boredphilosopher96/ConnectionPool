package org.example;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class TestDriver {
    static class ConnectionPoolTest extends Thread {
        private final String dbUrl = "jdbc:postgresql://localhost:5432/sumukhnitundila";
        DatabaseConnectionPool pool;

        public ConnectionPoolTest() {
            this.pool = new DatabaseConnectionPool(50, dbUrl);
        }

        @Override
        public void run() {
            try (Connection connection = pool.getConnection()) {
                System.out.println("Running thread : " + this.getName());
                connection.prepareStatement("SELECT pg_sleep(100)").execute();
                pool.putConnection(connection);
            } catch (SQLException | PoolException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void testConnectionPool() throws InterruptedException {
        long startTime = System.currentTimeMillis();
        ArrayList<Thread> threadList = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            Thread t1 = new ConnectionPoolTest();
            t1.setName("Thread " + i);
            threadList.add(t1);
            t1.start();
        }
        for (Thread thread : threadList) {
            thread.join();
        }
        long endTime = System.currentTimeMillis();
        System.out.println("Time taken in seconds : " + (endTime - startTime) / 1000d);
    }

    public void testWithoutConnectionPool() throws InterruptedException {
        System.out.println("Testing without connection pool");
        final String dbUrl = "jdbc:postgresql://localhost:5432/sumukhnitundila";
        ArrayList<Thread> threadList = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            Runnable t1 = () -> {
                try (Connection connection = DatabaseConnectionPool.createConnection(dbUrl)) {
                    connection.prepareStatement("SELECT pg_sleep(100)").execute();
                    System.out.println("Current Thread ID " + Thread.currentThread().getId());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

            };
            Thread thread = new Thread(t1);
            threadList.add(thread);
            thread.start();
        }

        for (Thread thread : threadList) {
            thread.join();
        }
    }

}
