package com.library_fine_calculation_system;

import com.library_fine_calculation_system.util.DBConnection;

import java.sql.Connection;

public class Main {

    public static void main(String[] args) {

        try (Connection connection = DBConnection.getConnection()) {

            System.out.println("Database Connected Successfully!");

        } catch (Exception e) {

            System.out.println("Database Connection Failed!");
            e.printStackTrace();

        }
    }
}
