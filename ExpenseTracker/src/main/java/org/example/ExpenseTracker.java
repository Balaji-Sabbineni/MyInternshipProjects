package org.example;

import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.UUID;

public class ExpenseTracker {
    private ArrayList<User> users;

    private Connection connection;

    public ExpenseTracker() throws SQLException {
        this.users = new ArrayList<>();
        this.connection = DatabaseConnection.getConnection();
    }

    public void userRegistration(String username, String password) throws SQLException, NoSuchAlgorithmException {

        PreparedStatement statement = connection.prepareStatement("INSERT INTO users (username, password) VALUES (?, ?)");
        statement.setString(1, username);
        statement.setString(2, password);

        int rowsAffected = statement.executeUpdate();
        if (rowsAffected == 1) {
            System.out.println("User registered successfully!");
        } else {
            System.out.println("Error registering user");
        }
    }

    public User userLogin(String username, String password) throws SQLException, NoSuchAlgorithmException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE username = ?");
        statement.setString(1, username);

        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            String storedPassword = resultSet.getString("password");
            if (password.equals(storedPassword)) {
                return new User(username, username);
            } else {
                System.out.println("Invalid password");
            }
        } else {
            System.out.println("User not found");
        }
        return null;
    }

    public void addExpense(User user) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Enter date (YYYY-MM-DD): ");
            String date = scanner.nextLine();
            System.out.print("Enter category: ");
            String category = scanner.nextLine();
            System.out.print("Enter amount: ");
            double amount = scanner.nextDouble();

            // Validate input (e.g., check date format, ensure positive amount)

            Expense expense = new Expense(date, category, amount);
            user.addExpense(expense);
            System.out.println("Expense added successfully!");
        } catch (Exception e) {
            System.err.println("Invalid input: Please enter a valid number for amount.");
        }
    }

    public void addExpense(User user, String date, String category, double amount) {
        Expense expense = new Expense(date, category, amount);
        user.addExpense(expense);
    }

    public void listExpenses(User user) {
        System.out.println("Expenses for " + user.getUsername() + ":");
        System.out.println("Date\tCategory\tAmount");
        for (Expense expense : user.getExpenses()) {
            System.out.println(expense.getDate() + "\t" + expense.getCategory() + "\t" + expense.getAmount());
        }
    }

    public void calculateCategoryTotals(User user) {
        HashMap<String, Double> categoryTotals = new HashMap<>();
        for (Expense expense : user.getExpenses()) {
            categoryTotals.putIfAbsent(expense.getCategory(), 0.0);
            categoryTotals.put(expense.getCategory(), categoryTotals.get(expense.getCategory()) + expense.getAmount());
        }

        System.out.println("Category Totals:");
        for (String category : categoryTotals.keySet()) {
            System.out.println(category + ": " + categoryTotals.get(category));
        }
    }

    public void saveExpenses(User user) {
        try (FileOutputStream fileOut = new FileOutputStream("expenses_" + user.getUsername() + ".ser");
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(user.getExpenses());
            System.out.println("Expenses saved successfully!");
        } catch (IOException e) {
            System.err.println("Error saving expenses: " + e.getMessage());
        }
    }

    public void loadExpenses(User user) {
        try (FileInputStream fileIn = new FileInputStream("expenses_" + user.getUsername() + ".ser");
             ObjectInputStream in = new ObjectInputStream(fileIn)) {
            ArrayList<Expense> loadedExpenses = (ArrayList<Expense>) in.readObject();
            user.setExpenses(loadedExpenses); // Assuming a setter for expenses in User class
            System.out.println("Expenses loaded successfully!");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading expenses: " + e.getMessage());
        }
    }

    public User getUserByUsername(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null; // User not found
    }

    public String generateExpenseListText(User currentUser) {
//        StringBuilder expenseListText = new StringBuilder();
//        expenseListText.append("Expenses for " + currentUser.getUsername() + ":\n");
//        expenseListText.append("Date\tCategory\tAmount\n");
//        for (Expense expense : currentUser.getExpenses()) {
//            expenseListText.append(expense.getDate() + "\t" + expense.getCategory() + "\t" + expense.getAmount() + "\n");
//        }
//        return expenseListText.toString();
        StringBuilder builder = new StringBuilder();
        builder.append("Expenses for ").append(currentUser.getUsername()).append(":\n\n");

        for (Expense expense : currentUser.getExpenses()) {
            builder.append("Date: ").append(expense.getDate()).append("\n");
            builder.append("Category: ").append(expense.getCategory()).append("\n");
            builder.append("Amount: ").append(String.format("%.2f", expense.getAmount())).append("\n\n");
        }

        return builder.toString();
    }


    public String calculateAndFormatCategoryTotals(User currentUser) {
        HashMap<String, Double> categoryTotals = new HashMap<>();
        for (Expense expense : currentUser.getExpenses()) {
            categoryTotals.putIfAbsent(expense.getCategory(), 0.0);
            categoryTotals.put(expense.getCategory(), categoryTotals.get(expense.getCategory()) + expense.getAmount());
        }
        return String.format("Category Totals:\n%s", categoryTotals.entrySet().stream().map(entry -> String.format("%s: %.2f\n", entry.getKey(), entry.getValue())));
    }


}
