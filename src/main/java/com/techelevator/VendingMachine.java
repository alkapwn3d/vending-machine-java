package com.techelevator;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.Set;

public class VendingMachine {
    private Scanner userInput = new Scanner(System.in);
    private Map<String, Product> inventory;
    private CustomerAccount account;

    public Map<String, Product> getInventory() {
        return inventory;
    }

    public void loadInventory(String filePath) {
        this.inventory = new Inventory(filePath).getInventory();
    }

    public void createAccount() {
        this.account = new CustomerAccount();
    }

    public CustomerAccount getAccount() {
        return account;
    }

    public void displayItem(Product product) {
        System.out.printf("%s %s $%.2f %s%n",
                product.getSlotLocation(),
                product.getName(),
                product.getPrice(),
                product.getType());
    }

    public void displayAllItems() {
        for (Product product : inventory.values()) {
            displayItem(product);
        }
    }

    public void selectProduct() {
        while (true) {
            displayAllItems();
            String slotLocation = promptForProductSelection();
            Product selectedProduct = searchItemBySlotLocation(slotLocation);

            if (selectedProduct == null) {
                System.out.println("Invalid product code or product is sold out. Please try again.");
            } else {
                if (account.getBalance() >= selectedProduct.getPrice()) {
                    account.subtractFromBalance(selectedProduct.getPrice());
                    selectedProduct.decrementQuantity();
                    dispenseProduct(selectedProduct);
                    break;
                } else {
                    System.out.println("Insufficient balance to purchase this item. Please try again.");
                }
            }
        }
    }

    private void dispenseProduct(Product product) {
        System.out.println("Dispensing " + product.getName());
        System.out.printf("Price: $%.2f%n", product.getPrice());
        System.out.printf("Money Remaining: $%.2f%n", account.getBalance());

        logTransaction(product.getName(), product.getPrice());
        String message = getMessageForProductType(product.getType());
        System.out.println(message);
    }

    private void feedMoney(double amount) {
        account.addToBalance(amount);
        logTransaction("FEED MONEY", amount);
    }

    public double promptForMoney() {
//        Scanner scanner = new Scanner(System.in);
        double moneyToAdd = 0.0;

        while (true) {
            System.out.print("Enter the amount to add (in dollars): $");

            if (userInput.hasNextDouble()) {
                moneyToAdd = userInput.nextDouble();

                if (moneyToAdd > 0) {
                    break;
                } else {
                    System.out.println("Please enter a valid positive amount.");
                }
            } else {
                System.out.println("Invalid input. Please enter a valid number.");
                userInput.next(); // Consume invalid input
            }
        }

        feedMoney(moneyToAdd);
        return moneyToAdd;
    }

    public boolean processPurchase(String productCode, int quantity) {
        Product productToPurchase = searchItemBySlotLocation(productCode);
        if (productToPurchase == null) {
            System.out.println("Product does not exist.");
            return false;
        }
        if (productToPurchase.isSoldOut()) {
            System.out.println("Product is sold out.");
            return false;
        }
        double totalCost = productToPurchase.getPrice() * quantity;
        if (account.getBalance() < totalCost) {
            System.out.println("Insufficient funds.");
            return false;
        }

        // Subtract the cost from the balance
        account.subtractFromBalance(totalCost);
        // "Sell" the item
        for (int i = 0; i < quantity; i++) {
            productToPurchase.decrementQuantity();
        }
        dispenseProduct(productToPurchase);

        // Log this action to a file
        logPurchase(productCode, quantity);

        return true; // indicates successful processing
    }

    private void logPurchase(String productCode, int quantity) {
        Product product = searchItemBySlotLocation(productCode);
        if (product == null) return;  // Additional validation can be implemented

        double totalCost = product.getPrice() * quantity;
        String transaction = String.format("%s Purchase: %s Quantity: %d Total Cost: $%.2f Remaining Balance: $%.2f",
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a")),
                product.getName(),
                quantity,
                totalCost,
                account.getBalance());

        writeTransactionLog(transaction, "Log.txt");
    }


    public void dispenseChange() {
        double change = account.giveChange();
        logTransaction("GIVE CHANGE", change);
    }

    public String getMessageForProductType(String productType) {
        switch (productType.toLowerCase()) {
            case "chip":
                return "Crunch Crunch, Yum!";
            case "candy":
                return "Munch Munch, Yum!";
            case "drink":
                return "Glug Glug, Yum!";
            case "gum":
                return "Chew Chew, Yum!";
            default:
                return "Yum Yum!";
        }
    }

    private String promptForProductSelection() {
        String slotLocation = "";
        Scanner input = new Scanner(System.in);
        while (true) {
            System.out.print("Enter the slot location of the product you want to purchase: ");
            slotLocation = input.nextLine();
            if (!isValidSlotLocation(slotLocation)) {
                System.out.println("Invalid slot location or product is sold out. Please try again.");
            } else {
                break;
            }
        }
        return slotLocation;
    }

    private boolean isValidSlotLocation(String slotLocation) {
        if (slotLocation.isEmpty()) {
            return false;
        }

        for (Product product : inventory.values()) {
            if (Objects.equals(product.getSlotLocation(), slotLocation)) {
                if (product.isSoldOut()) {
                    System.out.println("SOLD OUT: " + product.getName());
                } else {
                    return true;
                }
            }
        }
        return false;
    }

    private void logTransaction(String productName, double amount) {
        String transaction = String.format("%s %s $%.2f $%.2f",
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a")),
                productName,
                amount,
                account.getBalance());

        writeTransactionLog(transaction, "Log.txt");
    }

    private void writeTransactionLog(String transaction, String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
            writer.write(transaction);
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error writing the transaction to the log: " + e.getMessage());
        }
    }

    public void generateSalesReport() {
        String filename = String.format("SalesReport_%s.txt",
                currentTime("yyyyMMddHHmmss"));

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Product product : inventory.values()) {
                int quantityRemaining = 5 - product.getQuantity();
                String line = String.format("%s|%d",
                        //  product.getSlotLocation(),
                        product.getName(),
                        quantityRemaining
                        // product.getType()
                );
                writer.write("Sales Report Contents");
                writer.newLine();
                System.out.println("new file: " + filename);
            }

            String totalSales = String.format("\n**TOTAL SALES** $%.2f", calculateTotalSales());
            writer.write(totalSales);
            System.out.println("Sales report has been generated: " + filename);
        } catch (IOException e) {
            System.out.println("Error writing the sales report to a file: " + e.getMessage());
        }
    }

    public double calculateTotalSales() {
        double totalSales = 0.0;
        for (Product product : inventory.values()) {
            totalSales += product.getPrice() * (5 - product.getQuantity());
        }
        return totalSales;
    }

    public static String currentTime(String pattern) {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(pattern));
    }

    public Product searchItemBySlotLocation(String slotLocation) {
        for (Product product : inventory.values()) {
            if (Objects.equals(product.getSlotLocation(), slotLocation)) {
                if (!product.isSoldOut()) {
                    return product;
                }
            }
        }
        return null;
    }

}