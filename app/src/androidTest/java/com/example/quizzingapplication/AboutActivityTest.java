package com.example.quizzingapplication;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class AboutActivityTest {

    @Rule
    public ActivityScenarioRule<AboutActivity> activityRule =
            new ActivityScenarioRule<>(AboutActivity.class);

    // Test return to the main menu

    @Test
    public void clickMenuButton_goesBackToMainMenu() {
        onView(withId(R.id.menu_btn)).perform(click());
        onView(withId(R.id.quizzes_btn)).check(matches(isDisplayed()));
    }
}
