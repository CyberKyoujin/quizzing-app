package com.example.quizzingapplication;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule  =
            new ActivityScenarioRule<>(MainActivity.class);

    // Test navigation to the quiz list

    @Test
    public void clickQuizzesButton_opensQuizListActivity() {
        onView(withId(R.id.quizzes_btn)).perform(click());
        onView(withId(R.id.quizzes_recycler_view)).check(matches(isDisplayed()));
    }

    // Test navigation to the about activity

    @Test
    public void clickAboutButton_opensAboutActivity() {
        onView(withId(R.id.about_btn)).perform(click());
        onView(withId(R.id.menu_btn)).check(matches(isDisplayed()));
    }
}
