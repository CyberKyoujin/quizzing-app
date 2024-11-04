package com.example.quizzingapplication;

import java.util.List;

public class Quiz {
    private String name;
    private List<Question> questions;
    private int timerDuration;

    public Quiz (String name, List<Question> questions, int timerDuration){
        this.name = name;
        this.questions = questions;
        this.timerDuration = timerDuration;
    }

    public String getName() {
        return name;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public int getTimerDuration() {
        return timerDuration;
    }
}
