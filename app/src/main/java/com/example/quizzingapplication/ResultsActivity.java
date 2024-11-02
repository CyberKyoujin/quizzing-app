package com.example.quizzingapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ResultsActivity extends AppCompatActivity {

    private TextView scoreView;

    private Button menuButton;
    private Button restartButton;

    private int score;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.results_activity);

        score = getIntent().getIntExtra("score", 0);

        scoreView = findViewById(R.id.score_value_view);

        menuButton = findViewById(R.id.menu_btn);
        restartButton = findViewById(R.id.restart_quiz_btn);

        scoreView.setText(String.valueOf(score));

        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToMenu();
            }
        });

        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                restartQuiz();
            }
        });
    }

    private void restartQuiz () {
        Intent intent = new Intent(ResultsActivity.this, QuizActivity.class);
        startActivity(intent);
        finish();
    }

    private void navigateToMenu() {
        Intent intent = new Intent(ResultsActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
