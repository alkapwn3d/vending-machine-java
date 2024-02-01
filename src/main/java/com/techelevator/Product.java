package com.techelevator;

import com.techelevator.exceptions.ProductFormatException;

public class Product {
    private String slotLocation;
    private String name;
    private double price;
    private String type;

    private int quantity = 5;

    public Product() {
    }

    public Product(String slotLocation, String name, double price, String type) {
        this.slotLocation = slotLocation;
        this.name = name;
        this.price = price;
        this.type = type;
    }


    public String getSlotLocation() {
        return slotLocation;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getType() {
        return type;
    }

    public int getQuantity() {
        return quantity;
    }

    public void decrementQuantity() {
        if (quantity > 0) {
            quantity--;
        }
    }

    public boolean isSoldOut() {
        return quantity == 0;
    }

    public static Product toProduct(String string) throws ProductFormatException {
        String[] array = string.split("\\|");

        if (array.length != 4)
            throw new ProductFormatException("Invalid string format: " + string);

        String slot = array[0];
        String name = array[1];
        String type = array[3];

        double price;
        try {
            price = Double.parseDouble(array[2]);
        } catch (NumberFormatException e) {
            throw new ProductFormatException("Invalid number format: " + array[2]);
        }

        return new Product(slot, name, price, type);
    }
}
