package org.example;

import java.util.Scanner;

public class LinkShortenerCLI {

    public static void main(String[] args) {
        URLShortener shortener = new URLShortener();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Enter 1 to shorten a URL, 2 to expand a URL, or 3 to exit:");
            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice == 1) {
                System.out.print("Enter the long URL: ");
                String longURL = scanner.nextLine();
                String shortURL = shortener.shortenURL(longURL);
                System.out.println("Shortened URL: " + shortURL);
            } else if (choice == 2) {
                System.out.print("Enter the short URL: ");
                String shortURL = scanner.nextLine();
                String longURL = shortener.expandURL(shortURL);
                if (longURL != null) {
                    System.out.println("Long URL: " + longURL);
                } else {
                    System.out.println("Invalid short URL.");
                }
            } else if (choice == 3) {
                break;
            } else {
                System.out.println("Invalid choice.");
            }
        }
    }
}