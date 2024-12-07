package com.example.quizzingapplication;


import static com.google.common.truth.Truth.assertThat;


import android.os.Parcel;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;


public class QuestionTest {


    // Test question creation

    @Test
    public void testQuestionParcelable() {
        List<String> options = Arrays.asList("Option1", "Option2", "Option3");
        List<Integer> answers = Arrays.asList(0, 2);
        Question original = new Question("Sample question?", "multiple choice", options, answers);

        Parcel parcel = Parcel.obtain();
        original.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);

        Question createdFromParcel = Question.CREATOR.createFromParcel(parcel);
        parcel.recycle();

        assertThat(createdFromParcel.getText()).isEqualTo("Sample question?");
        assertThat(createdFromParcel.getType()).isEqualTo("multiple choice");
        assertThat(createdFromParcel.getOptions()).containsExactlyElementsIn(options);
        assertThat(createdFromParcel.getCorrectAnswers()).containsExactlyElementsIn(answers);
    }
}