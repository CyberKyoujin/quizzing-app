package com.example.quizzingapplication;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

@RunWith(AndroidJUnit4.class)
public class QuizActivityTest {

    private Intent intent;

    // Initial setup with a test quiz

    @Before
    public void setup() throws Exception {
        Quiz testQuiz = new Quiz("Test Quiz",
                java.util.Collections.singletonList(new Question("Test Q?", "single",
                        java.util.Arrays.asList("A", "B"), java.util.Collections.singletonList(0))), 30);

        intent = createQuizIntent(testQuiz);
    }

    // Check whether the IU is displayed
    @Test
    public void initialUIDisplayed() {
        ActivityScenario<QuizActivity> scenario = ActivityScenario.launch(intent);

        onView(withId(R.id.text_question)).check(matches(isDisplayed()));
        onView(withId(R.id.score_view_res)).check(matches(withText("0")));
        onView(withId(R.id.title_view)).check(matches(withText("Test Quiz")));
    }

    // Check whether the questions are displayed
    @Test
    public void testDisplaysQuestion() {
        ActivityScenario<QuizActivity> scenario = ActivityScenario.launch(intent);
        onView(withId(R.id.text_question)).check(matches(isDisplayed()));
    }

    // Check score update after correct answer
    @Test
    public void testSingleChoiceCorrectUpdatesScore() {
        ActivityScenario<QuizActivity> scenario = ActivityScenario.launch(intent);

        // Click on the correct answer
        onView(withText("A")).perform(click());

        onView(withId(R.id.score_view_res)).check(matches(withText("1")));
    }

    // Check score doesn't update after incorrect answer
    @Test
    public void testSingleChoiceIncorrectDoesNotUpdateScore() {
        ActivityScenario<QuizActivity> scenario = ActivityScenario.launch(intent);

        // Click on the incorrect answer
        onView(withText("B")).perform(click());

        onView(withId(R.id.score_view_res)).check(matches(withText("0")));
    }

    // Check navigation to the ResultsActivity

    @Test
    public void finishQuizNavigatesToResults() {
        ActivityScenario<QuizActivity> scenario = ActivityScenario.launch(intent);

        onView(withId(R.id.finish_btn)).perform(click());

        onView(withId(R.id.score_res_value)).check(matches(isDisplayed()));
    }

    // Check score update after correct answer on multiple question
    @Test
    public void multipleChoiceCorrectAnswers() throws Exception {
        Quiz multiQuiz = new Quiz("Multi Quiz",
                Collections.singletonList(
                        new Question("Select correct answers", "multiple choice",
                                Arrays.asList("Opt1", "Opt2", "Opt3"), Arrays.asList(0, 2))
                ), 30);

        Intent multiIntent = createQuizIntent(multiQuiz);
        ActivityScenario<QuizActivity> scenario = ActivityScenario.launch(multiIntent);

        onView(withText("Opt1")).perform(click());
        onView(withText("Opt3")).perform(click());

        onView(withId(R.id.submit_btn)).perform(click());

        onView(withId(R.id.score_view_res)).check(matches(withText("1")));
    }

    public static Intent createQuizIntent(Quiz quiz) {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), QuizActivity.class);
        intent.putExtra("quiz_name", quiz.getName());
        intent.putExtra("quiz_timer_duration", quiz.getTimerDuration());
        // quiz.getQuestions() must be Parcelable, ensure your Question class implements Parcelable correctly.
        intent.putParcelableArrayListExtra("quiz_questions", new ArrayList<>(quiz.getQuestions()));
        return intent;
    }

}
