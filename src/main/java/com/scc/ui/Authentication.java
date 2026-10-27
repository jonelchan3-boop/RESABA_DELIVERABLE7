package com.scc.ui;

import com.scc.dao.UserDao;
import com.scc.model.User;
import java.util.Scanner;

public class Authentication {
    private final Scanner sc = new Scanner(System.in);
    private final UserDao userDao = new UserDao();

    public void start() {
        System.out.println("\n===== USER LOGIN =====");
        System.out.print("Username: ");
        String uname = sc.nextLine().trim();
        System.out.print("Password: ");
        String pwd = sc.nextLine().trim();

        User user = userDao.getUserByUsername(uname);
        if (user == null) {
            System.out.println("[✗] Username not found.");
            return;
        }
        if (!user.getPassword().equals(pwd)) {
            System.out.println("[✗] Incorrect password.");
            return;
        }
        if (!"ACTIVE".equals(user.getAccountStatus()) && !"APPROVED".equals(user.getAccountStatus())) {
            System.out.println("[!] Account not approved. Wait for admin approval.");
            return;
        }

        System.out.println("[✓] Login SUCCESSFUL! Welcome, " + user.getUsername() + " [" + user.getRole() + "]");

        switch (user.getRole()) {
            case "ADMIN" -> new AdminMenu(user).show(); // ✅ FIXED: pass 'user'
            case "STAFF/CASHIER" -> new StaffMenu().show();
            case "DELIVERY RIDER" -> new DeliveryRiderMenu().show();
            case "CUSTOMER" -> new CustomerMenu().show();
            default -> System.out.println("[✗] Unknown role.");
        }
    }
}