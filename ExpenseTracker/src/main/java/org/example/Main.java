package org.example;

import javax.swing.*;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new UserInterface();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }
}