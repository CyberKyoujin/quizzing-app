package com.example.quizzingapplication;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import android.content.Context;

import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.File;
import java.util.List;

public class QuizSaverTest {

    private CreateQuizActivity createQuizActivity;
    private QuizListActivity quizListActivity;

    @Before
    public void setUp() {
        createQuizActivity = mock(CreateQuizActivity.class);
        quizListActivity = mock(QuizListActivity.class);
    }

    @Test
    public void testSaveQuiz() {
        Quiz quiz = new Quiz("Sample Quiz", List.of(new Question("Question 1", "single", List.of("Option 1", "Option 2"), List.of(0))), 10);
        File file = new File("quizzes.json");

        when(createQuizActivity.saveQuiz(quiz)).thenReturn(true);

        boolean result = createQuizActivity.saveQuiz(quiz);
        assertTrue(result);
    }

    @Test
    public void testLoadQuizzes() {
        List<Quiz> mockedQuizzes = List.of(new Quiz("Mock Quiz", List.of(new Question("Mock Question", "multiple", List.of("Option 1", "Option 2"), List.of(0))), 10));

        when(quizListActivity.loadQuizzes()).thenReturn(mockedQuizzes);

        List<Quiz> quizzes = quizListActivity.loadQuizzes();
        assertNotNull(quizzes);
        assertTrue(quizzes.size() > 0);
    }
}