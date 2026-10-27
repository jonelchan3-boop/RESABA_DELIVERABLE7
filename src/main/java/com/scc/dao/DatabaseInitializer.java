package com.scc.dao;

import com.scc.model.User;
import java.sql.Connection;
import java.sql.Statement;

public class DatabaseInitializer {
    public static void init() {
        try (Connection conn = DatabaseConnection.connect();
             Statement stmt = conn.createStatement()) {

            stmt.execute("PRAGMA foreign_keys = ON;");

            String createUsers = """
                CREATE TABLE IF NOT EXISTS Users (
                    User_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                    Username TEXT NOT NULL UNIQUE,
                    Password TEXT NOT NULL,
                    Role TEXT NOT NULL CHECK(Role IN ('ADMIN', 'STAFF/CASHIER', 'DELIVERY RIDER', 'CUSTOMER')),
                    AccountStatus TEXT NOT NULL DEFAULT 'PENDING'
                    CHECK(AccountStatus IN ('PENDING', 'APPROVED', 'REJECTED', 'ACTIVE', 'SUSPENDED'))
                )
                """;
            stmt.execute(createUsers);

            String createCustomers = """
                CREATE TABLE IF NOT EXISTS Customers (
                    Customer_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                    CustomerName TEXT NOT NULL,
                    ContactNumber TEXT,
                    Address TEXT,
                    User_ID INTEGER,
                    FOREIGN KEY (User_ID) REFERENCES Users(User_ID) ON DELETE SET NULL
                )
                """;
            stmt.execute(createCustomers);

            String createOrders = """
                CREATE TABLE IF NOT EXISTS Orders (
                    Order_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                    Customer_ID INTEGER NOT NULL,
                    OrderDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    Quantity INTEGER NOT NULL,
                    TotalAmount REAL NOT NULL,
                    OrderStatus TEXT DEFAULT 'PENDING',
                    FOREIGN KEY (Customer_ID) REFERENCES Customers(Customer_ID) ON DELETE CASCADE
                )
                """;
            stmt.execute(createOrders);

            String createDeliveries = """
                CREATE TABLE IF NOT EXISTS Deliveries (
                    Delivery_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                    Order_ID INTEGER NOT NULL,
                    Rider_ID INTEGER,
                    DeliveryAddress TEXT,
                    DeliveryDate TIMESTAMP,
                    DeliveryStatus TEXT DEFAULT 'PENDING',
                    FOREIGN KEY (Order_ID) REFERENCES Orders(Order_ID) ON DELETE CASCADE,
                    FOREIGN KEY (Rider_ID) REFERENCES Users(User_ID) ON DELETE SET NULL
                )
                """;
            stmt.execute(createDeliveries);

            String createLogs = """
                CREATE TABLE IF NOT EXISTS ActivityLogs (
                    Log_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                    User_ID INTEGER NOT NULL,
                    Action TEXT NOT NULL,
                    Details TEXT,
                    LogDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    FOREIGN KEY (User_ID) REFERENCES Users(User_ID) ON DELETE CASCADE
                )
                """;
            stmt.execute(createLogs);

            UserDao userDao = new UserDao();
            if (userDao.getUserByUsername("admin") == null) {
                User defaultAdmin = new User("admin", "admin123", "ADMIN", "ACTIVE");
                userDao.addUser(defaultAdmin);
                System.out.println("[✓] Default Admin — Username: admin | Password: admin123");
            }

            System.out.println("[✓] Database initialized successfully.");
        } catch (Exception e) {
            System.out.println("[✗] DB Init Error: " + e.getMessage());
        }
    }
}