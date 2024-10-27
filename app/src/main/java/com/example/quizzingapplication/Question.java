package com.example.quizzingapplication;

import java.util.List;

public class Question {

    private String text;
    private String type;
    private List<String> options;
    private List<Integer> correctAnswers;

    public Question(String text, String type, List<String> options, List<Integer> correctAnswers){
        this.text = text;
        this.type = type;
        this.options = options;
        this.correctAnswers = correctAnswers;
    }

    public String getText() {
        return text;
    }

    public String getType() {
        return type;
    }

    public List<Integer> getCorrectAnswers() {
        return correctAnswers;
    }

    public List<String> getOptions() {
        return options;
    }
}
