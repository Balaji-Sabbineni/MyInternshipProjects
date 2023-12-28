package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
public class QuizApp {
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String args[]) {
        System.out.print("Enter your name: ");
        String name = scanner.nextLine();
        System.out.println("Welcome to the quiz, " + name + "!");
        int score =0;

        Quiz quiz = createQuiz();
        for (int i = 0; i < quiz.getNumberOfQuestions(); i++) {
            Question question = quiz.getQuestion(i);
            System.out.println();
            System.out.println(question.getText());
            displayOptions(question);

            int answerChoice = getValidatedAnswerChoice(question);

            if(handleAnswer(question,answerChoice)){
                System.out.println("Correct answer! ");
                System.out.println();
                score++;
            }else{
                System.out.println("Incorrect answer. The correct answer was: " + question.getOptions()[question.getCorrectAnswerIndex()]);
                System.out.println();
            }
        }

        System.out.println("Congratulations, " + name + "! You have completed the quiz. Your score is: " + score);
    }

    private static Quiz createQuiz() {
        Quiz quiz = new Quiz();
        try (BufferedReader reader = new BufferedReader(new FileReader("*CSV FILE PATH*"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] questionData = line.split(","); // Assuming CSV format is: question text,option1,option2,...,correctAnswerIndex
                Question question = new Question(questionData[0], Arrays.copyOfRange(questionData, 1, questionData.length - 1), Integer.parseInt(questionData[questionData.length - 1]));
                quiz.addQuestion(question);
            }
        } catch (IOException e) {
            System.out.println("Error reading CSV file: " + e.getMessage());
            return null;
        }
        return quiz;
    }

    private static void displayOptions(Question question) {
        for (int i = 0; i < question.getOptions().length; i++) {
            System.out.println((i + 1) + ". " + question.getOptions()[i]);
        }
    }

    private static int getValidatedAnswerChoice(Question question) {
        while (true) {
            System.out.print("Enter your answer choice (1-" + question.getOptions().length + "): ");
            if (scanner.hasNextInt()) {
                int answerChoice = scanner.nextInt();
                if (answerChoice >= 1 && answerChoice <= question.getOptions().length) {
                    return answerChoice - 1; // Adjust for 0-based indexing
                } else {
                    System.out.println("Invalid answer choice. Please enter a number between 1 and " + question.getOptions().length);
                }
            } else {
                scanner.next();
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    private static boolean handleAnswer(Question question, int answerChoice) {
        if (answerChoice == question.getCorrectAnswerIndex()) {
            return true;
        } else {
            return false;
        }
    }
}
