package com.example.quizzingapplication;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import androidx.test.core.app.ApplicationProvider;

public class QuizAdapterTest {

    @Mock
    private QuizAdapter.OnQuizClickListener mockListener;

    private List<Quiz> quizList;
    private QuizAdapter quizAdapter;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        quizList = Arrays.asList(new Quiz("Sample Quiz", Arrays.asList(new Question("Question 1", "single", Arrays.asList("Option 1", "Option 2"), Arrays.asList(0))), 10));
        quizAdapter = new QuizAdapter(ApplicationProvider.getApplicationContext(), quizList, mockListener);
    }

    @Test
    public void testGetItemCount() {
        assertEquals(1, quizAdapter.getItemCount());
    }

    @Test
    public void testOnBindViewHolder() {
        // Get the application context using ApplicationProvider
        Context context = ApplicationProvider.getApplicationContext();

        // Inflate a real view for the ViewHolder
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.quiz_item, null);
        QuizAdapter.QuizViewHolder viewHolder = new QuizAdapter.QuizViewHolder(itemView);

        // Bind the view holder with the data at position 0
        quizAdapter.onBindViewHolder(viewHolder, 0);

        // Verify if the quiz name is set correctly in the ViewHolder
        TextView quizNameTextView = viewHolder.quizName;
        assertNotNull(quizNameTextView);
        assertEquals("Sample Quiz", quizNameTextView.getText().toString());
    }
}
