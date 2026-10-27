package com.scc.ui;

import java.util.Scanner;

public class StaffMenu {
    private final Scanner sc = new Scanner(System.in);

    public void show() {
        while (true) {
            System.out.println("\n=====================================");
            System.out.println("       STAFF / CASHIER DASHBOARD     ");
            System.out.println("=====================================");
            System.out.println("1. View All Orders");
            System.out.println("2. Create New Order");
            System.out.println("3. Update Order Status");
            System.out.println("4. Search Customer");
            System.out.println("5. Logout");
            System.out.print("Choice: ");

            switch (sc.nextLine().trim()) {
                case "1" -> System.out.println("[✓] Showing all orders...");
                case "2" -> System.out.println("[✓] New order form opened...");
                case "3" -> System.out.println("[✓] Updating order status...");
                case "4" -> System.out.println("[✓] Searching customer...");
                case "5" -> { System.out.println("Logged out."); return; }
                default -> System.out.println("[!] Invalid choice.");
            }
        }
    }
}