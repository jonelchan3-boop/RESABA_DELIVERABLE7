package com.scc.ui;

import java.util.Scanner;

public class DeliveryRiderMenu {
    private final Scanner sc = new Scanner(System.in);

    public void show() {
        while (true) {
            System.out.println("\n=====================================");
            System.out.println("       DELIVERY RIDER DASHBOARD      ");
            System.out.println("=====================================");
            System.out.println("1. View Assigned Deliveries");
            System.out.println("2. Mark Delivery as Picked Up");
            System.out.println("3. Mark Delivery as Delivered");
            System.out.println("4. Update Delivery Status");
            System.out.println("5. Logout");
            System.out.print("Choice: ");

            switch (sc.nextLine().trim()) {
                case "1" -> System.out.println("[✓] Showing assigned deliveries...");
                case "2" -> System.out.println("[✓] Delivery marked as Picked Up.");
                case "3" -> System.out.println("[✓] Delivery marked as Delivered.");
                case "4" -> System.out.println("[✓] Updating delivery status...");
                case "5" -> { System.out.println("Logged out."); return; }
                default -> System.out.println("[!] Invalid choice.");
            }
        }
    }
}