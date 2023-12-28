package org.example;

import java.util.ArrayList;
import java.util.List;

public class Quiz {
    List<Question> questions;

    public Quiz() {
        this.questions = new ArrayList<>();
    }

    public void addQuestion(Question question) {
        questions.add(question);
    }

    public Question getQuestion(int index) {
        return questions.get(index);
    }

    public int getNumberOfQuestions() {
        return questions.size();
    }
}
