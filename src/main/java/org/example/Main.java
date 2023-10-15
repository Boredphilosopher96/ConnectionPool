package org.example;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        try {
            TestDriver td = new TestDriver();
            td.testConnectionPool();
//            td.testWithoutConnectionPool();

        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            System.out.println("Could not read property file properly");
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
    }
}