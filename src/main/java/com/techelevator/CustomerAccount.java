package com.techelevator;

public class CustomerAccount {
    private double balance = 0.0;

    public double getBalance() {
        return balance;
    }

    public void addToBalance(double amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Cannot add a negative amount");
        }
        balance += amount;
    }


    public void subtractFromBalance(double amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Cannot subtract a negative amount");
        }
        if (balance < amount) {
            throw new IllegalArgumentException("Insufficient balance");
        }
        balance -= amount;
    }

    public double giveChange() {
        double change = balance;
        balance = 0.0;
        return change;
    }
}
