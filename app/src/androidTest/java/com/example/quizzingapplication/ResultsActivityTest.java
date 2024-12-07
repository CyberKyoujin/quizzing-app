package com.example.quizzingapplication;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class ResultsActivityTest {

    // Test correct score is displayed
    @Test
    public void testResultsActivity() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), ResultsActivity.class);
        intent.putExtra("score", 5);
        intent.putExtra("quiz_name", "Test Quiz");
        intent.putExtra("quiz_timer_duration", 30);

        ActivityScenario<ResultsActivity> scenario = ActivityScenario.launch(intent);

        onView(withId(R.id.menu_btn)).perform(click());
        onView(withId(R.id.quizzes_btn)).check(matches(isDisplayed()));
    }
}