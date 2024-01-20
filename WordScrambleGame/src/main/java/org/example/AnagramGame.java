package org.example;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class AnagramGame extends JFrame {
    private JLabel scrambledWordLabel;
    private JTextField guessTextField;
    private JButton checkButton;
    private JButton newWordButton;
    private JButton quitButton;
    private JLabel scoreLabel;
    private JTextArea correctAnswerTextArea;

    private List<String> wordList;
    private String currentWord;
    private int score;
    private int totalWordsAnswered;

    public AnagramGame() {
        setTitle("Anagram Word Scramble Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 400);
        setLayout(new GridLayout(6, 1));

        initializeWordsFromFile("C://Users/lenovo/Downloads/1-1000.txt");
        initializeComponents();

        displayNextWord();

        setVisible(true);
    }

    private void initializeWordsFromFile(String filePath) {
        wordList = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                wordList.add(line.trim().toLowerCase());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initializeComponents() {
        scrambledWordLabel = new JLabel();
        scrambledWordLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        add(scrambledWordLabel);

        guessTextField = new JTextField();
        add(guessTextField);

        checkButton = new JButton("Check Guess");
        checkButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkGuess();
            }
        });
        add(checkButton);

        newWordButton = new JButton("New Word");
        newWordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayNextWord();
            }
        });
        add(newWordButton);

        quitButton = new JButton("Quit Game");
        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                quitGame();
            }
        });
        add(quitButton);

        correctAnswerTextArea = new JTextArea();
        correctAnswerTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(correctAnswerTextArea);
        add(scrollPane);

        scoreLabel = new JLabel("Score: 0/0");
        add(scoreLabel);
    }

    private void displayNextWord() {
        correctAnswerTextArea.setText(""); // Clear the correct answer text area
        if (wordList.isEmpty()) {
            showGameOverMessage();
            System.exit(0);
        }

        // Select a random word from the list
        Random random = new Random();
        int randomIndex = random.nextInt(wordList.size());
        currentWord = wordList.get(randomIndex);

        // Scramble the word
        List<Character> charList = new ArrayList<>();
        for (char c : currentWord.toCharArray()) {
            charList.add(c);
        }
        Collections.shuffle(charList);

        StringBuilder scrambledWord = new StringBuilder();
        for (char c : charList) {
            scrambledWord.append(c);
        }

        scrambledWordLabel.setText(scrambledWord.toString());
    }

    private void checkGuess() {
        String userGuess = guessTextField.getText().trim().toLowerCase();

        totalWordsAnswered++;

        if (userGuess.equals(currentWord)) {
            displayCorrectMessage();
            wordList.remove(currentWord);
            guessTextField.setText("");
            score++;
        } else {
            displayIncorrectMessage();
            guessTextField.setText("");
        }

        updateScoreLabel();
    }

    private void updateScoreLabel() {
        scoreLabel.setText("Score: " + score + "/" + totalWordsAnswered);
    }

    private void quitGame() {
        showGameOverMessage();
        System.exit(0);
    }

    private void showGameOverMessage() {
        JOptionPane.showMessageDialog(this, "Congratulations! You've completed the game. Total Score: " + score + "/" + totalWordsAnswered);
    }

    private void displayCorrectMessage() {
        SwingUtilities.invokeLater(() -> {
            correctAnswerTextArea.append("Correct! Well done.\n");
        });
    }

    private void displayIncorrectMessage() {
        SwingUtilities.invokeLater(() -> {
            correctAnswerTextArea.append("Wrong. Correct answer is: " + currentWord);
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new AnagramGame();
            }
        });
    }
}
