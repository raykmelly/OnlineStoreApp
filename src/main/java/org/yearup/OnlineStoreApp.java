package org.yearup;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Scanner;

public class OnlineStoreApp {
    // Global variables for use in multiple functions
    public Scanner scanner = new Scanner(System.in);
    public HashMap<Integer,Product> inventoryMap = new HashMap<>();
    public static ArrayList<Product> cart = new ArrayList<>();
    public void run(){
        // Load items from the csv file into the inventory Map
        loadInventory();
        // Start the loop and display the home screen
        homeScreen();
    }

    private void promptSale(double total){
        System.out.print("Enter amount paid: $");

        try {
            double reply = scanner.nextDouble();
            double remainder = Math.floor((total - reply) * 100) / 100;

            if (remainder <= 0 ) {
                // Change due
                remainder = Math.abs(remainder);

                System.out.println("\n[Receipt]");
                for (Product product :  cart) {
                    System.out.println("-" + product.getName());
                }
                System.out.println("Change due: $" + remainder + "\n");

                cart = new ArrayList<>();
                homeScreen();
            }else {
                // Lack of funds
                System.out.println("Lack of funds, refund issued\n");
                promptSale(total);
            }

        } catch (InputMismatchException e) {
            // Incorrect input catcher
            System.out.println("Please use an double / float to reply\n");

            scanner.nextLine();
            promptCart(total);
        }
    }
    private void promptCart(double total) {
        while (true) {
            System.out.println("[Select an option from those below]");
            System.out.println("1.) Check out");
            System.out.println("0.) Return to home screen");
            System.out.print("Enter: ");

            try {
                int reply = scanner.nextInt();
                switch (reply) {
                    case 1:
                        // Check out
                        System.out.println("Checking out...\n");
                        promptSale(total);

                        return;
                    case 0:
                        // Return to home screen
                        System.out.println("Returning to home screen...\n");
                        homeScreen();

                        return;
                    default:
                        // Catch incorrect input
                        System.out.println("Unknown input, please try again\n");

                        promptCart(total);
                }

            } catch (InputMismatchException e) {
                // Incorrect input catcher
                System.out.println("Please use an integer to reply\n");

                scanner.nextLine();
                promptCart(total);
            }
        }
    }

    private void displayCart() {
        System.out.println("[The following items are in your cart]");

        double total = 0;
        for (Product product :  cart) {
            System.out.println("-" + product.getName());
            total += product.getPrice();
        }

        total = Math.floor(total * 100) / 100;
        System.out.println("Total: $" + total + "\n");

        promptCart(total);
    }

    private void promptProducts() {
        while (true) {
            System.out.println("[Enter the product ID or '0' to return to Home Screen]");
            System.out.print("Enter: ");

            try {
                int reply = scanner.nextInt();
                if (inventoryMap.get(reply) != null) {
                    // Add item to cart
                    Product product = inventoryMap.get(reply);
                    System.out.println("Adding " + product.getName() + " to cart...\n");

                    cart.add(product);
                } else if (reply == 0){
                    // Return to home screen
                    System.out.println("Returning to home screen...\n");

                    homeScreen();
                    return;
                } else {
                    // Unknown product catcher
                    System.out.println("Unknown product ID, please try again...\n");

                    promptProducts();
                }

            } catch (InputMismatchException e) {
                // Incorrect input catcher
                System.out.println("Please use an integer to reply...\n");

                scanner.nextLine();
                promptProducts();
            }
        }
    }
    private void displayProducts() {
        for (Integer id :  inventoryMap.keySet()) {
            Product product = inventoryMap.get(id);

            System.out.println("Id: " + product.getId());
            System.out.println("Name: " + product.getName());
            System.out.println("Price: $" + product.getPrice() + "\n");
        }

        promptProducts();
    }
    public void homeScreen(){
        while (true) {
            System.out.println("[Select an option from those below]");
            System.out.println("1.) Show products");
            System.out.println("2.) Show cart");
            System.out.println("0.) Exit application");
            System.out.print("Enter: ");

            try {
                int reply = scanner.nextInt();
                switch (reply) {
                    case 1:
                        System.out.println("Showing products...\n");
                        displayProducts();

                        return;
                    case 2:
                        System.out.println("Showing cart...\n");
                        displayCart();

                        return;
                    case 0:
                        System.out.println("Exiting application...\n");
                        System.exit(0);

                        return;
                    default:
                        System.out.println("Please enter a valid choice\n");

                }
            } catch (InputMismatchException e) {
                System.out.println("Please use an integer to reply\n");

                scanner.nextLine();
                homeScreen();
            }
        }
    }
    public void loadInventory(){
        try (Scanner reader = new Scanner(new File("inventory.csv"))){
            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                String[] split = line.split("\\|");

                int id = Integer.parseInt(split[0]);
                String name = split[1];
                double price = Double.parseDouble(split[2]);

                Product product = new Product(id,name,price);

                inventoryMap.put(id,product);
            }
        } catch (FileNotFoundException e){
            System.out.println("Error: " + e);
        }
    }
}
