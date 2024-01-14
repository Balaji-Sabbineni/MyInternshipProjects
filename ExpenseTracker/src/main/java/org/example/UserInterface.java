package org.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

public class UserInterface extends JFrame implements ActionListener {

    private JTextField usernameField, passwordField;
    private JButton registerButton, loginButton;
    private JPanel combinedPanel, mainPanel;
    private JTable expenseTable;
    private ExpenseTracker expenseTracker;
    private User currentUser;

    public UserInterface() throws SQLException {
        super("Expense Tracker");
        expenseTracker = new ExpenseTracker();

        // Combined login/registration panel
        combinedPanel = new JPanel();
        combinedPanel.setLayout(new GridLayout(4, 2));
        combinedPanel.add(new JLabel("Username:"));
        combinedPanel.add(usernameField = new JTextField());
        combinedPanel.add(new JLabel("Password:"));
        combinedPanel.add(passwordField = new JPasswordField());
        combinedPanel.add(registerButton = new JButton("Register"));
        registerButton.addActionListener(this);
        combinedPanel.add(loginButton = new JButton("Login"));
        loginButton.addActionListener(this);

        setContentPane(combinedPanel);

        // Main panel (initialized for later use)
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        // Expense table
        expenseTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(expenseTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == registerButton) {
            try {
                handleUserRegistration();
            } catch (SQLException | NoSuchAlgorithmException ex) {
                JOptionPane.showMessageDialog(this, "Error creating user: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getSource() == loginButton) {
            try {
                handleUserLogin();
            } catch (SQLException | NoSuchAlgorithmException ex) {
                JOptionPane.showMessageDialog(this, "Error logging in: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            // Handle other button actions (add expense, list expenses, etc.)
        }
    }

    private void handleUserRegistration() throws SQLException, NoSuchAlgorithmException {
        String username = usernameField.getText();
        String password = passwordField.getText();
        expenseTracker.userRegistration(username, password);
        JOptionPane.showMessageDialog(this, "Registration successful!");
    }

    private void handleUserLogin() throws SQLException, NoSuchAlgorithmException {
        String username = usernameField.getText();
        String password = passwordField.getText();
        User user = expenseTracker.userLogin(username, password);

        if (user != null) {
            currentUser = user;
            populateExpenseTable();
            switchToMainView();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid username or password", "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void switchToMainView() {
        setContentPane(mainPanel);
        mainPanel.add(createExpenseButtons(), BorderLayout.SOUTH);
        pack();
    }

    private JPanel createExpenseButtons() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 5));

        JButton addExpenseButton = new JButton("Add Expense");
        addExpenseButton.addActionListener(e -> handleAddExpense());
        buttonPanel.add(addExpenseButton);

        JButton listExpensesButton = new JButton("List Expenses");
        listExpensesButton.addActionListener(e -> handleListExpenses());
        buttonPanel.add(listExpensesButton);

        JButton calculateTotalsButton = new JButton("Category Totals");
        calculateTotalsButton.addActionListener(e -> handleCalculateCategoryTotals());
        buttonPanel.add(calculateTotalsButton);

        JButton saveExpensesButton = new JButton("Save Expenses");
        saveExpensesButton.addActionListener(e -> handleSaveExpenses());
        buttonPanel.add(saveExpensesButton);

        JButton loadExpensesButton = new JButton("Load Expenses");
        loadExpensesButton.addActionListener(e -> handleLoadExpenses());
        buttonPanel.add(loadExpensesButton);

        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> handleLogout());
        buttonPanel.add(logoutButton);

        return buttonPanel;
    }

    private void handleAddExpense() {
        JTextField dateField = new JTextField();
        JTextField categoryField = new JTextField();
        JTextField amountField = new JTextField();

        int result = JOptionPane.showConfirmDialog(this, new Object[]{
                "Date:", dateField,
                "Category:", categoryField,
                "Amount:", amountField
        }, "Add Expense", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            try {
                String date = dateField.getText();
                String category = categoryField.getText();
                double amount = Double.parseDouble(amountField.getText());
                expenseTracker.addExpense(currentUser, date, category, amount);
                populateExpenseTable();
                JOptionPane.showMessageDialog(this, "Expense added successfully!");
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid amount format. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void handleListExpenses() {
        String expensesText = expenseTracker.generateExpenseListText(currentUser); // Get formatted list as text
        JOptionPane.showMessageDialog(this, expensesText, "Expenses", JOptionPane.PLAIN_MESSAGE);
    }

    private void handleCalculateCategoryTotals() {
        String categoryTotalsText = expenseTracker.calculateAndFormatCategoryTotals(currentUser);
        JOptionPane.showMessageDialog(this, categoryTotalsText, "Category Totals", JOptionPane.PLAIN_MESSAGE);
    }

    private void handleSaveExpenses() {
        expenseTracker.saveExpenses(currentUser);
        JOptionPane.showMessageDialog(this, "Expenses saved successfully!");
    }

    private void handleLoadExpenses() {
        expenseTracker.loadExpenses(currentUser);
        populateExpenseTable();
        JOptionPane.showMessageDialog(this, "Expenses loaded successfully!");
    }


    private void populateExpenseTable() {
        DefaultTableModel model = new DefaultTableModel(new String[]{"Date", "Category", "Amount"}, 0);
        for (Expense expense : currentUser.getExpenses()) {
            model.addRow(new Object[]{expense.getDate(), expense.getCategory(), expense.getAmount()});
        }
        expenseTable.setModel(model);
    }

    private void handleLogout() {
        currentUser = null;
        passwordField.setText("");
        setContentPane(combinedPanel); // Switch back to the combined panel
        pack(); // Resize the window to fit the content
    }
}
