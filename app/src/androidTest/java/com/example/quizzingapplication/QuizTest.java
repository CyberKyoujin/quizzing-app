package com.example.quizzingapplication;

import static com.google.common.truth.Truth.assertThat;

import android.os.Parcel;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;



public class QuizTest {

    // Test Quiz creation

    @Test
    public void quizParcelable_roundTrip_succeeds() {
        List<String> options = Arrays.asList("Option1", "Option2");
        List<Integer> answers = Arrays.asList(1);
        Question question = new Question("Is this a test?", "single", options, answers);
        Quiz originalQuiz = new Quiz("Sample Quiz", Arrays.asList(question), 30);

        Parcel parcel = Parcel.obtain();
        originalQuiz.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);

        Quiz createdFromParcel = Quiz.CREATOR.createFromParcel(parcel);
        parcel.recycle();

        assertThat(createdFromParcel.getName()).isEqualTo("Sample Quiz");
        assertThat(createdFromParcel.getTimerDuration()).isEqualTo(30);
        assertThat(createdFromParcel.getQuestions()).hasSize(1);
        assertThat(createdFromParcel.getQuestions().get(0).getText()).isEqualTo("Is this a test?");
    }
}
