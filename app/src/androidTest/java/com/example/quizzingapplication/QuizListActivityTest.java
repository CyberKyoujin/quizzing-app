package com.example.quizzingapplication;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.*;

import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItem;
import static androidx.test.espresso.contrib.RecyclerViewActions.scrollToHolder;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItem;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

import static java.util.function.Predicate.not;

import android.content.Context;
import android.view.View;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.hamcrest.Matcher;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;

@RunWith(AndroidJUnit4.class)
public class QuizListActivityTest {

    @BeforeClass
    public static void setup() throws Exception {
        Context context = ApplicationProvider.getApplicationContext();


        JSONArray quizArray = new JSONArray();

        // Quiz 1
        JSONObject quiz1 = new JSONObject();
        quiz1.put("name", "Quiz One");
        quiz1.put("timer_duration", 30);
        quiz1.put("questions", new JSONArray());

        // Quiz 2
        JSONObject quiz2 = new JSONObject();
        quiz2.put("name", "Quiz Two");
        quiz2.put("timer_duration", 60);
        quiz2.put("questions", new JSONArray());

        quizArray.put(quiz1);
        quizArray.put(quiz2);

        File file = new File(context.getFilesDir(), "quizzes.json");
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(quizArray.toString().getBytes(StandardCharsets.UTF_8));
        }
    }

    @Rule
    public ActivityScenarioRule<QuizListActivity> activityRule =
            new ActivityScenarioRule<>(QuizListActivity.class);

    // Check whether the quizzes are displayed

    @Test
    public void testLoadedQuizzes() {

        onView(withId(R.id.quizzes_recycler_view)).check(matches(isDisplayed()));

        onView(withText("Quiz Two")).check(matches(isDisplayed()));
    }

    // Check navigation to the CreateQuizActivity
    @Test
    public void testOpenCreateQuizActivity() {
        // Click on the create quiz button
        onView(withId(R.id.create_quiz_btn)).perform(click());
        // Check that CreateQuizActivity UI element is displayed
        onView(withId(R.id.button_add_question)).check(matches(isDisplayed()));
    }

    // Check delete quiz
    @Test
    public void testDeletesQuiz() {
        // Open delete dialog
        onView(withId(R.id.quizzes_recycler_view))
                .perform(actionOnItem(hasDescendant(withText("Quiz One")),
                        clickChildViewWithId(R.id.delete_btn)));

        onView(withText("Yes")).perform(click());


        onView(withText("Quiz One")).check(doesNotExist());
    }

    // Check cancel delete

    @Test
    public void testDeleteQuizKeepsQuiz() {
        onView(withId(R.id.quizzes_recycler_view)).perform(actionOnItem(hasDescendant(withText("Quiz Two")), clickChildViewWithId(R.id.delete_btn)));

        onView(withText("No")).inRoot(isDialog()).perform(click());

        onView(withText("Quiz Two")).check(matches(isDisplayed()));
    }

    public static ViewAction clickChildViewWithId(final int id) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isDisplayed();
            }

            @Override
            public String getDescription() {
                return "Click on a child view with specified id.";
            }

            @Override
            public void perform(UiController uiController, View view) {
                View childView = view.findViewById(id);
                if (childView != null) {
                    childView.performClick();
                }
            }
        };
    }

}