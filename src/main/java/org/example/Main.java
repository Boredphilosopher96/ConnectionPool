package org.example;

import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;
import java.util.logging.Logger;

import org.postgresql.*;

public class Main {
    Logger logger = Logger.getLogger(toString());

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