package org.example;

public class Expense {
    private String date;
    private String category;
    private double amount;

    // Constructor to create an Expense object with initial values
    public Expense(String date, String category, double amount) {
        this.date = date;
        this.category = category;
        this.amount = amount;
    }

    // Getters for all fields
    public String getDate() {
        return date;
    }

    public String getCategory() {
        return category;
    }

    public double getAmount() {
        return amount;
    }

    // Setter for category (consider if allowing date or amount changes is necessary)
    public void setCategory(String category) {
        this.category = category;
    }
}
