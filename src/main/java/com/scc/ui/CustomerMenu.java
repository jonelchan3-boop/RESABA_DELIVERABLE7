package com.scc.ui;

import java.util.Scanner;

public class CustomerMenu {
    private final Scanner sc = new Scanner(System.in);

    public void show() {
        while (true) {
            System.out.println("\n=====================================");
            System.out.println("          CUSTOMER DASHBOARD         ");
            System.out.println("=====================================");
            System.out.println("1. Place New Order");
            System.out.println("2. View My Orders");
            System.out.println("3. Track Delivery Status");
            System.out.println("4. Update My Profile");
            System.out.println("5. Logout");
            System.out.print("Choice: ");

            switch (sc.nextLine().trim()) {
                case "1" -> System.out.println("[✓] New order form opened...");
                case "2" -> System.out.println("[✓] Showing your orders...");
                case "3" -> System.out.println("[✓] Tracking delivery...");
                case "4" -> System.out.println("[✓] Profile update form opened...");
                case "5" -> { System.out.println("Logged out."); return; }
                default -> System.out.println("[!] Invalid choice.");
            }
        }
    }
}