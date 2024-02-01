package com.techelevator;

import com.techelevator.exceptions.ProductFormatException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Inventory {
    private String filePath;
    //private Set<Product> inventory = new HashSet<>();

    private Map<String,Product> inventory = new TreeMap<>();
    public Inventory(String filePath) {
        this.filePath = filePath;
        loadInventory();
    }

    public Map<String,Product> getInventory() {
        return inventory;
    }

    public int getSize() {
        return inventory.size();
    }

    private void loadInventory() {
        try {
            for (String string : readFile()) {
                Product product = Product.toProduct(string);


              //  inventory.add(product);
                inventory.put(product.getSlotLocation(),product);
            }
        } catch (ProductFormatException e) {
            System.out.println("Invalid product format" + e);
        }
    }

    private List<String> readFile() {
        List<String> listOfStrings = null;
        try {
            listOfStrings = Files.readAllLines(Path.of(filePath));
        } catch (IOException e) {
            System.out.println("File not found: " + filePath);
        }
        return listOfStrings;
    }
}
