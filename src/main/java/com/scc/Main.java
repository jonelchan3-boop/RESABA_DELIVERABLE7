package com.scc;

import com.scc.dao.DatabaseInitializer;
import com.scc.dao.UserDao;
import com.scc.model.User;
import com.scc.ui.Authentication;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        DatabaseInitializer.init();
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n===== DRINKING WATER REFILL & DELIVERY SYSTEM =====");
            System.out.println("1. LOGIN");
            System.out.println("2. REGISTER");
            System.out.println("3. EXIT");
            System.out.print("Enter choice: ");

            switch (sc.nextLine().trim()) {
                case "1" -> new Authentication().start();
                case "2" -> {
                    System.out.println("\n---------- USER REGISTRATION ----------");
                    System.out.print("Username: ");
                    String regUname = sc.nextLine().trim();
                    if (regUname.isEmpty()) {
                        System.out.println("[!] Username cannot be empty.");
                        break;
                    }
                    System.out.print("Password: ");
                    String regPwd = sc.nextLine().trim();
                    if (regPwd.isEmpty()) {
                        System.out.println("[!] Password cannot be empty.");
                        break;
                    }

                    System.out.println("\n--- SELECT ROLE ---");
                    System.out.println("1. ADMIN");
                    System.out.println("2. STAFF/CASHIER");
                    System.out.println("3. DELIVERY RIDER");
                    System.out.println("4. CUSTOMER");
                    System.out.print("Enter choice: ");
                    String regRole = null;
                    while (regRole == null) {
                        String c = sc.nextLine().trim();
                        switch (c) {
                            case "1" -> regRole = "ADMIN";
                            case "2" -> regRole = "STAFF/CASHIER";
                            case "3" -> regRole = "DELIVERY RIDER";
                            case "4" -> regRole = "CUSTOMER";
                            default -> System.out.print("[!] Invalid — choose 1–4: ");
                        }
                    }

                    User regUser = new User(regUname, regPwd, regRole, "PENDING");
                    if (new UserDao().addUser(regUser)) {
                        System.out.println("[SUCCESS] Registered! Wait for Admin approval.");
                    } else {
                        System.out.println("[FAILED] Username exists or invalid role.");
                    }
                    break;
                }
                case "3" -> {
                    System.out.println("Exiting... Goodbye!");
                    sc.close();
                    return;
                }
                default -> System.out.println("[INVALID] Select 1, 2, or 3 only.");
            }
        }
    }
}