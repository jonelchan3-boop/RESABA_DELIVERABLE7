package com.scc.ui;

import com.scc.dao.UserDao;
import com.scc.model.User;
import java.util.List;
import java.util.Scanner;

public class AdminMenu {
    private final Scanner sc = new Scanner(System.in);
    private final UserDao userDao = new UserDao();
    private final User currentAdmin; // To store the logged-in admin

    // Constructor to receive the currently logged-in admin
    public AdminMenu(User currentAdmin) {
        this.currentAdmin = currentAdmin;
    }

    // ========== INPUT VALIDATION WRAPPERS ADDED HERE ==========
    // Safe integer input — catches non-numeric so app doesn't crash
    private int readIntInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("[!] Invalid input! Enter a number only.");
            }
        }
    }

    // Only accept valid roles — keep asking until correct
    private String readValidRole(String prompt) {
        List<String> validRoles = List.of(
                "ADMIN", "STAFF/CASHIER", "DELIVERY RIDER", "CUSTOMER"
        );
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim().toUpperCase();
            if ("0".equals(input)) return "0"; // Allow cancel
            if (validRoles.contains(input)) {
                return input;
            }
            System.out.println("[!] Invalid role! Use: ADMIN | STAFF/CASHIER | DELIVERY RIDER | CUSTOMER");
        }
    }

    // Only accept APPROVED or PENDING — keep asking until correct
    private String readValidStatus(String prompt) {
        List<String> validStatuses = List.of("APPROVED", "PENDING");
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim().toUpperCase();
            if ("0".equals(input)) return "0"; // Allow cancel
            if (validStatuses.contains(input)) {
                return input;
            }
            System.out.println("[!] Invalid status! Use: APPROVED | PENDING");
        }
    }
    // ==========================================================

    private void displayUserTable(List<User> users) {
        System.out.println("-----------------------------------------------------------");
        System.out.printf("%-5s | %-15s | %-18s | %-10s%n", "ID", "USERNAME", "ROLE", "STATUS");
        System.out.println("-----------------------------------------------------------");
        for (User u : users) {
            System.out.printf("%-5d | %-15s | %-18s | %-10s%n",
                    u.getUserId(), u.getUsername(), u.getRole(), u.getAccountStatus());
        }
        System.out.println("-----------------------------------------------------------");
    }

    public void show() {
        while (true) {
            System.out.println("\n=====================================");
            System.out.println("          ADMIN DASHBOARD            ");
            System.out.println("=====================================");
            System.out.println("1. View All Users");
            System.out.println("2. View Pending Approvals");
            System.out.println("3. Approve User");
            System.out.println("4. Add New User");
            System.out.println("5. Update User Role/Status");
            System.out.println("6. Delete User");
            System.out.println("7. Logout");
            System.out.print("Choice: ");

            switch (sc.nextLine().trim()) {
                case "1" -> viewAllUsers();
                case "2" -> viewPendingApprovals();
                case "3" -> approveRegistration();
                case "4" -> addNewUser();
                case "5" -> updateUserInfo();
                case "6" -> deleteUser();
                case "7" -> {
                    System.out.println("Logged out.");
                    return;
                }
                default -> System.out.println("[!] Invalid choice.");
            }
        }
    }

    private void viewAllUsers() {
        System.out.println("\n--- ALL USERS ---");
        List<User> list = userDao.getAllUsers();
        if (list.isEmpty()) {
            System.out.println("No users found.");
            return;
        }
        displayUserTable(list);
    }

    private void viewPendingApprovals() {
        System.out.println("\n--- PENDING APPROVALS ---");
        List<User> pending = userDao.getPendingUsers();
        if (pending.isEmpty()) {
            System.out.println("No pending registrations.");
            return;
        }
        displayUserTable(pending);
    }

    private void approveRegistration() {
        viewPendingApprovals();
        List<User> pending = userDao.getPendingUsers();
        if (pending.isEmpty()) return;

        // Using readIntInput instead of direct parseInt
        int id = readIntInput("\nEnter User ID to approve (0 = Cancel): ");
        if (id == 0) {
            System.out.println("Approval cancelled.");
            return;
        }
        boolean exists = pending.stream().anyMatch(u -> u.getUserId() == id);
        if (!exists) {
            System.out.println("[!] ID not in pending list.");
            return;
        }
        if (userDao.approveUser(id)) {
            System.out.println("[✓] User approved! Status → APPROVED");
        } else {
            System.out.println("[✗] Approval failed.");
        }
    }

    private void addNewUser() {
        System.out.println("\n--- ADD NEW USER ---");
        System.out.print("Username: ");
        String uname = sc.nextLine().trim();
        if (uname.isEmpty()) {
            System.out.println("Empty username.");
            return;
        }

        System.out.print("Password: ");
        String pwd = sc.nextLine().trim();
        if (pwd.isEmpty()) {
            System.out.println("Empty password.");
            return;
        }

        // Role selection with choices
        System.out.println("\n--- SELECT ROLE ---");
        System.out.println("1. ADMIN");
        System.out.println("2. STAFF/CASHIER");
        System.out.println("3. DELIVERY RIDER");
        System.out.println("4. CUSTOMER");
        System.out.println("0 = Cancel");
        String role = null;
        while (role == null) {
            System.out.print("Enter choice: ");
            String choice = sc.nextLine().trim();
            switch (choice) {
                case "1" -> role = "ADMIN";
                case "2" -> role = "STAFF/CASHIER";
                case "3" -> role = "DELIVERY RIDER";
                case "4" -> role = "CUSTOMER";
                case "0" -> {
                    System.out.println("Cancelled.");
                    return;
                }
                default -> System.out.println("[!] Invalid choice — select 0–4 only.");
            }
        }

        // Status selection with choices
        System.out.println("\n--- SELECT STATUS ---");
        System.out.println("1. APPROVED");
        System.out.println("2. PENDING");
        System.out.println("0 = Cancel");
        String status = null;
        while (status == null) {
            System.out.print("Enter choice: ");
            String choice = sc.nextLine().trim();
            switch (choice) {
                case "1" -> status = "APPROVED";
                case "2" -> status = "PENDING";
                case "0" -> {
                    System.out.println("Cancelled.");
                    return;
                }
                default -> System.out.println("[!] Invalid choice — select 0–2 only.");
            }
        }

        if (userDao.addUser(new User(uname, pwd, role, status))) {
            System.out.println("[✓] User added successfully.");
        } else {
            System.out.println("[✗] Failed. Username exists or invalid role.");
        }
    }

    private void updateUserInfo() {
        viewAllUsers();

        // Using readIntInput — no crash on invalid input
        int targetId = readIntInput("\nUser ID to update (0 = Cancel): ");
        if (targetId == 0) {
            System.out.println("Update cancelled.");
            return;
        }

        // ========== SELF-EDIT RESTRICTION ADDED ==========
        if (targetId == currentAdmin.getUserId()) {
            System.out.println("WARNING: Modifying the currently active admin account is restricted.");
            return;
        }
        // =================================================

        System.out.print("Update Role? (Y/N): ");
        String newRole = null;
        if (sc.nextLine().trim().equalsIgnoreCase("Y")) {
            newRole = readValidRole("New Role (0 = Cancel): ");
            if ("0".equals(newRole)) {
                System.out.println("Update cancelled.");
                return;
            }
        }

        System.out.print("Update Status? (Y/N): ");
        String newStatus = null;
        if (sc.nextLine().trim().equalsIgnoreCase("Y")) {
            newStatus = readValidStatus("New Status (0 = Cancel): ");
            if ("0".equals(newStatus)) {
                System.out.println("Update cancelled.");
                return;
            }
        }

        // If both changed — use the new combined method
        if (newRole != null && newStatus != null) {
            if (userDao.updateUserRoleAndStatus(targetId, newRole, newStatus)) {
                System.out.println("[✓] Role and Status updated successfully.");
            } else {
                System.out.println("[✗] Update failed.");
            }
        }
        // If only role changed
        else if (newRole != null) {
            if (userDao.updateRole(targetId, newRole)) {
                System.out.println("[✓] Role updated.");
            } else {
                System.out.println("[✗] Invalid role.");
            }
        }
        // If only status changed
        else if (newStatus != null) {
            if (userDao.updateStatus(targetId, newStatus)) {
                System.out.println("[✓] Status updated.");
            } else {
                System.out.println("[✗] Update failed.");
            }
        }
    }

    private void deleteUser() {
        viewAllUsers();

        // Using readIntInput
        int id = readIntInput("\nUser ID to DELETE (0 = Cancel): ");
        if (id == 0) return;

        // Self-edit restriction for delete too
        if (id == currentAdmin.getUserId()) {
            System.out.println("WARNING: Modifying the currently active admin account is restricted.");
            return;
        }

        System.out.print("Confirm DELETE? YES/NO: ");
        if (sc.nextLine().trim().equalsIgnoreCase("YES")) {
            if (userDao.deleteUser(id)) System.out.println("[✓] Deleted.");
            else System.out.println("[✗] Failed.");
        }
    }
}