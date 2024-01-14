package org.example;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class User {
    private String username;
    private String passwordHash;
    private ArrayList<Expense> expenses;

    // Constructor to create a new User object
    public User(String username, String password) throws NoSuchAlgorithmException {
        this.username = String.valueOf(username);
        this.passwordHash = generatePasswordHash(password);
        this.expenses = new ArrayList<>();
    }

    // Getters for username and expenses
    public String getUsername() {
        return username;
    }

    public ArrayList<Expense> getExpenses() {
        return expenses;
    }


    // Method to generate a secure hash of the password using SHA-256
    private String generatePasswordHash(String password) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(password.getBytes());
        return new String(hash);
    }

    // Method to add an expense to the user's list
    public void addExpense(Expense expense) {
        expenses.add(expense);
    }
    public void setExpenses(ArrayList<Expense> expenses) {
        this.expenses = expenses;
    }
}
