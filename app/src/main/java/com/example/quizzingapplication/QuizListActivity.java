package com.example.quizzingapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizzingapplication.Question;
import com.example.quizzingapplication.Quiz;
import com.example.quizzingapplication.QuizActivity;
import com.example.quizzingapplication.QuizAdapter;
import com.example.quizzingapplication.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class QuizListActivity extends AppCompatActivity implements QuizAdapter.OnQuizClickListener {

    private RecyclerView quizzesRecyclerView;
    private List<Quiz> quizList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_list_activity);

        quizzesRecyclerView = findViewById(R.id.quizzes_recycler_view);

        quizList = loadQuizzes();
        if (quizList.isEmpty()) {
            Toast.makeText(this, "No quizzes available", Toast.LENGTH_LONG).show();
        }

        QuizAdapter adapter = new QuizAdapter(this, quizList, this);
        quizzesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        quizzesRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onQuizClick(Quiz quiz) {

        Intent intent = new Intent(this, QuizActivity.class);
        intent.putExtra("quiz_name", quiz.getName());
        intent.putExtra("quiz_timer_duration", quiz.getTimerDuration());
        intent.putParcelableArrayListExtra("quiz_questions", (ArrayList<? extends Parcelable>) quiz.getQuestions());
        startActivity(intent);
    }

    private List<Quiz> loadQuizzes() {
        List<Quiz> quizList = new ArrayList<>();

        try {
            InputStream is = getAssets().open("quizzes.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            String json = new String(buffer, "UTF-8");
            JSONArray quizArray = new JSONArray(json);

            for (int i = 0; i < quizArray.length(); i++) {
                JSONObject quizObject = quizArray.getJSONObject(i);

                String name = quizObject.getString("name");
                int timerDuration = quizObject.getInt("timer_duration");

                JSONArray questionsArray = quizObject.getJSONArray("questions");
                List<Question> questionList = new ArrayList<>();

                for (int j = 0; j < questionsArray.length(); j++) {
                    JSONObject questionObject = questionsArray.getJSONObject(j);

                    String text = questionObject.getString("text");
                    String type = questionObject.getString("type");

                    JSONArray optionsArray = questionObject.getJSONArray("options");
                    List<String> options = new ArrayList<>();
                    for (int k = 0; k < optionsArray.length(); k++) {
                        options.add(optionsArray.getString(k));
                    }

                    JSONArray answersArray = questionObject.getJSONArray("answers");
                    List<Integer> correctAnswers = new ArrayList<>();
                    for (int k = 0; k < answersArray.length(); k++) {
                        correctAnswers.add(answersArray.getInt(k));
                    }

                    Question question = new Question(text, type, options, correctAnswers);
                    questionList.add(question);
                }

                Quiz quiz = new Quiz(name, questionList, timerDuration);
                quizList.add(quiz);
            }

        } catch (IOException | JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error loading quizzes", Toast.LENGTH_LONG).show();
        }

        return quizList;
    }
}



