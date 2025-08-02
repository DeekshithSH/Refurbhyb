package com.example;

import java.io.File;
import java.sql.*;
import java.util.Scanner;

class Buyer {
    int phoneNo;
    String email;
    String name;

    Buyer(int phoneNo, String email, String name) {
        this.phoneNo = phoneNo;
        this.email = email;
        this.name = name;
    }
}

public class Main {
    static Connection conn;
    static Scanner in = new Scanner(System.in);

    public static void main(String[] args) {
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:./test.db");
            System.out.println("Opened database successfully");
            createTables();

            // ToDO: Replace CLI with Working GUI
            mainMenu();
        } catch (Exception e) {
            System.err.println("Database Error: " + e.getMessage());
        } finally {
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException e) {
                System.err.println("Closing Error: " + e.getMessage());
            }
        }
    }

    static void createTables() throws SQLException {
        Statement stmt = conn.createStatement();
        try {
            stmt.executeUpdate("""
                        CREATE TABLE IF NOT EXISTS Buyer (
                            email VARCHAR(50) PRIMARY KEY,
                            name VARCHAR(50),
                            phoneNo BIGINT,
                            password VARCHAR(50)
                        );
                    """);

            stmt.executeUpdate("""
                        CREATE TABLE IF NOT EXISTS Category (
                            id INTEGER PRIMARY KEY AUTOINCREMENT,
                            name VARCHAR(50) UNIQUE
                        );
                    """);

            stmt.executeUpdate("""
                        CREATE TABLE IF NOT EXISTS Item (
                            id VARCHAR(36) PRIMARY KEY,
                            name VARCHAR(50),
                            model VARCHAR(50),
                            category_id INTEGER,
                            FOREIGN KEY (category_id) REFERENCES Category(id)
                        );
                    """);

            stmt.executeUpdate("""
                        CREATE TABLE IF NOT EXISTS Cart (
                            id INTEGER PRIMARY KEY AUTOINCREMENT,
                            email VARCHAR(50),
                            item_id VARCHAR(36),
                            quantity INTEGER DEFAULT 1,
                            created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                            FOREIGN KEY (email) REFERENCES Buyer(email),
                            FOREIGN KEY (item_id) REFERENCES Item(id)
                        );
                    """);

            stmt.executeUpdate("""
                        CREATE TABLE IF NOT EXISTS Metadata (
                            key VARCHAR(50) PRIMARY KEY,
                            value VARCHAR(50)
                        );
                    """);
        } finally {
            stmt.close();
        }

    }

    static void mainMenu() throws SQLException {
        while (true) {
            printMenu("Main Menu", new String[] { "Buyer", "Seller", "Exit" });
            switch (in.nextInt()) {
                case 1:
                    buyerMenu();
                    break;
                case 2:
                    System.out.println("Seller module: To be implemented.");
                    break;
                case 3:
                    System.out.println("Exiting...");
                    return;
            }
        }
    }

    static void buyerMenu() throws SQLException {
        while (true) {
            printMenu("Buyer Menu", new String[] { "Login", "Signup", "Back" });
            switch (in.nextInt()) {
                case 1:
                    loginBuyer();
                    break;
                case 2:
                    signupBuyer();
                    break;
                case 3:
                    return;
            }
        }
    }

    static void loginBuyer() throws SQLException {
        System.out.print("Email: ");
        String email = in.next();
        System.out.print("Password: ");
        String pass = in.next();

        String sql = "SELECT * FROM Buyer WHERE email=? AND password=?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, email);
        stmt.setString(2, pass);

        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            System.out.println("Login Successful!");
            Buyer buyer = new Buyer(rs.getInt("phoneNo"), rs.getString("email"), rs.getString("name"));
            buyerDashboard(buyer);
        } else {
            System.out.println("Invalid credentials.");
        }

        stmt.close();
    }

    static void signupBuyer() throws SQLException {
        System.out.print("Name: ");
        String name = in.next();
        System.out.print("Email: ");
        String email = in.next();
        System.out.print("Phone No: ");
        int phone = in.nextInt();
        System.out.print("Password: ");
        String pass = in.next();

        String sql = "INSERT INTO Buyer (name, email, phoneNo, password) VALUES (?, ?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, name);
        stmt.setString(2, email);
        stmt.setInt(3, phone);
        stmt.setString(4, pass);

        try {
            stmt.executeUpdate();
            System.out.println("Signup successful.");
        } catch (SQLException e) {
            System.out.println("Signup failed: " + e.getMessage());
        }

        stmt.close();
    }

    static void buyerDashboard(Buyer buyer) throws SQLException {
        while (true) {
            printMenu("Buyer Dashboard", new String[] { "Show Cart", "Search Item", "Back" });
            switch (in.nextInt()) {
                case 1:
                    showCart(buyer.email);
                    break;
                case 2:
                    searchItem();
                    break;
                case 3:
                    return;
            }
        }
    }

    static void showCart(String email) throws SQLException {
        String sql = "SELECT Item.name, Cart.id FROM Cart JOIN Item ON Cart.item_id = Item.id WHERE Cart.email=?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, email);
        ResultSet rs = stmt.executeQuery();

        boolean found = false;
        while (rs.next()) {
            found = true;
            System.out.println(rs.getString("name") + "\t: " + rs.getInt("id"));
        }
        if (!found) {
            System.out.println("Your cart is empty.");
        }

        stmt.close();
    }

    static void searchItem() throws SQLException {
        System.out.print("Enter item name to search: ");
        String search = in.next();

        String sql = "SELECT * FROM Item WHERE name LIKE ? LIMIT 10";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, "%" + search + "%");

        ResultSet rs = stmt.executeQuery();

        boolean found = false;
        while (rs.next()) {
            found = true;
            System.out.println(rs.getString("name") + "\t: " + rs.getInt("id"));
        }

        if (!found) {
            System.out.println("No items found.");
        }

        stmt.close();
    }

    static void printMenu(String title, String[] options) {
        System.out.println("\n== " + title + " ==");
        for (int i = 0; i < options.length; i++) {
            System.out.println((i + 1) + ". " + options[i]);
        }
        System.out.print("Choice: ");
    }
}
