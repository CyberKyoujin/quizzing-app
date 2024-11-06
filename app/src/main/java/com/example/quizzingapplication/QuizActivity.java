package com.example.quizzingapplication;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class QuizActivity extends AppCompatActivity {

    private TextView questionView;
    private TextView scoreView;
    private TextView titleView;
    private TextView questionTypeView;
    private TextView repeatQuestionsView;
    private TextView timerView;

    private Button firstOption;
    private Button secondOption;
    private Button thirdOption;
    private Button fourthOption;

    private CheckBox firstCheckbox;
    private CheckBox secondCheckbox;
    private CheckBox thirdCheckbox;
    private CheckBox fourthCheckbox;

    private Button submitBtn;
    private Button finishBtn;

    private int currentQuestionIndex = 0;
    private int score = 0;
    private int repeatCount = 0;
    private final int MAX_REPEATS = 1;
    private int selectedOptionIndex = -1;
    private Question currentQuestion;

    private CountDownTimer timer;
    private long timeLeft = 20000;

    private String quizName;
    private int timerDuration;
    private List<Question> questionList;
    private List<Question> incorrectQuestions = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_activity);

        questionView = findViewById(R.id.text_question);
        scoreView = findViewById(R.id.score_view);
        questionTypeView = findViewById(R.id.question_type_view);
        repeatQuestionsView = findViewById(R.id.repeat_questions_view);
        timerView = findViewById(R.id.timer_view);
        titleView = findViewById(R.id.title_view);

        firstOption = findViewById(R.id.button_option1);
        secondOption = findViewById(R.id.button_option2);
        thirdOption = findViewById(R.id.button_option3);
        fourthOption = findViewById(R.id.button_option4);

        firstCheckbox = findViewById(R.id.checkbox_option1);
        secondCheckbox = findViewById(R.id.checkbox_option2);
        thirdCheckbox = findViewById(R.id.checkbox_option3);
        fourthCheckbox = findViewById(R.id.checkbox_option4);

        submitBtn = findViewById(R.id.submit_btn);
        finishBtn = findViewById(R.id.finish_btn);

        repeatQuestionsView.setVisibility(View.GONE);

        quizName = getIntent().getStringExtra("quiz_name");
        timerDuration = getIntent().getIntExtra("quiz_timer_duration", 30);
        questionList = getIntent().getParcelableArrayListExtra("quiz_questions");

        if (questionList != null && !questionList.isEmpty()) {
            setTitle(quizName);

            currentQuestion = questionList.get(currentQuestionIndex);
            displayQuestion(currentQuestion);
        } else {
            Toast.makeText(this, "No questions available", Toast.LENGTH_LONG).show();
            finish();
        }

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer(currentQuestion);
            }
        });

        finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishQuiz();
            }
        });

        scoreView.setText(String.valueOf(score));
        titleView.setText(quizName);
    }

    private void displayQuestion(Question question) {

        resetVisibility();
        resetButtonColors();
        resetCheckboxColors();

        questionView.setText(question.getText());
        List<String> options = question.getOptions();
        List<Integer> answers = question.getCorrectAnswers();
        String questionType = question.getType();

        if (Objects.equals(questionType, "multiple choice")) {
            // Multiple-answer question: checkboxes
            setCheckboxes(options);
            questionTypeView.setText(String.format("Multiple - %d correct answers", answers.size()));
            submitBtn.setVisibility(View.VISIBLE);
        } else if (Objects.equals(questionType, "single")) {
            // Single-answer question: buttons
            setButtons(options);
            questionTypeView.setText("Single");
        }

        startTimer(timerDuration);
    }

    private void loadNextQuestion() {
        currentQuestionIndex++;

        if (currentQuestionIndex < questionList.size()) {
            currentQuestion = questionList.get(currentQuestionIndex);
            displayQuestion(currentQuestion);
            timeLeft = timerDuration * 1000L;
        } else if (!incorrectQuestions.isEmpty() && repeatCount < MAX_REPEATS) {
            questionList = new ArrayList<>(incorrectQuestions);
            incorrectQuestions.clear();
            currentQuestionIndex = 0;
            currentQuestion = questionList.get(currentQuestionIndex);
            repeatCount++;
            Toast.makeText(this, "Repeating incorrect questions", Toast.LENGTH_SHORT).show();
            repeatQuestionsView.setVisibility(View.VISIBLE);
            displayQuestion(currentQuestion);
            timeLeft = timerDuration * 1000L;
        } else {
            Toast.makeText(this, "Quiz Finished!", Toast.LENGTH_SHORT).show();
            finishQuiz();
        }
    }

    private void finishQuiz(){
        if (timer != null) {
            timer.cancel();
        }

        Intent intent = new Intent(QuizActivity.this, ResultsActivity.class);
        intent.putExtra("score", score);
        startActivity(intent);
        finish();
    }

    private void resetVisibility() {
        firstOption.setVisibility(View.GONE);
        secondOption.setVisibility(View.GONE);
        thirdOption.setVisibility(View.GONE);
        fourthOption.setVisibility(View.GONE);

        firstCheckbox.setVisibility(View.GONE);
        secondCheckbox.setVisibility(View.GONE);
        thirdCheckbox.setVisibility(View.GONE);
        fourthCheckbox.setVisibility(View.GONE);

        submitBtn.setVisibility(View.GONE);
    }

    private void setButtons(List<String> options) {
        if (options.size() > 0) {
            firstOption.setText(options.get(0));
            firstOption.setVisibility(View.VISIBLE);
            firstOption.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedOptionIndex = 0;
                    checkAnswer(currentQuestion);
                }
            });
        }

        if (options.size() > 1) {
            secondOption.setText(options.get(1));
            secondOption.setVisibility(View.VISIBLE);
            secondOption.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedOptionIndex = 1;
                    checkAnswer(currentQuestion);
                }
            });
        }

        if (options.size() > 2) {
            thirdOption.setText(options.get(2));
            thirdOption.setVisibility(View.VISIBLE);
            thirdOption.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedOptionIndex = 2;
                    checkAnswer(currentQuestion);
                }
            });
        }

        if (options.size() > 3) {
            fourthOption.setText(options.get(3));
            fourthOption.setVisibility(View.VISIBLE);
            fourthOption.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedOptionIndex = 3;
                    checkAnswer(currentQuestion);
                }
            });
        }
    }

    private void setCheckboxes(List<String> options) {
        if (options.size() > 0) {
            firstCheckbox.setText(options.get(0));
            firstCheckbox.setVisibility(View.VISIBLE);
        }
        if (options.size() > 1) {
            secondCheckbox.setText(options.get(1));
            secondCheckbox.setVisibility(View.VISIBLE);
        }
        if (options.size() > 2) {
            thirdCheckbox.setText(options.get(2));
            thirdCheckbox.setVisibility(View.VISIBLE);
        }
        if (options.size() > 3) {
            fourthCheckbox.setText(options.get(3));
            fourthCheckbox.setVisibility(View.VISIBLE);
        }
    }

    private void resetCheckboxes() {
        firstCheckbox.setChecked(false);
        secondCheckbox.setChecked(false);
        thirdCheckbox.setChecked(false);
        fourthCheckbox.setChecked(false);
    }

    private void highlightButton(int index, int color) {
        ColorStateList highlightColor = ColorStateList.valueOf(color);

        switch (index) {
            case 0:
                firstOption.setBackgroundTintList(highlightColor);
                break;
            case 1:
                secondOption.setBackgroundTintList(highlightColor);
                break;
            case 2:
                thirdOption.setBackgroundTintList(highlightColor);
                break;
            case 3:
                fourthOption.setBackgroundTintList(highlightColor);
                break;
            default:
                break;
        }
    }

    private void highlightCheckbox(int index, int color) {

        GradientDrawable dottedBorder = (GradientDrawable) ContextCompat.getDrawable(this, R.drawable.dotted_border);

        if (dottedBorder != null) {
            dottedBorder.setStroke(2, ContextCompat.getColor(this, color), 4, 2);
        }

        switch (index) {
            case 0:
                firstCheckbox.setBackground(dottedBorder);
                break;
            case 1:
                secondCheckbox.setBackground(dottedBorder);
                break;
            case 2:
                thirdCheckbox.setBackground(dottedBorder);
                break;
            case 3:
                fourthCheckbox.setBackground(dottedBorder);
                break;
            default:
                break;
        }
    }

    private void disableButtons() {
        firstOption.setEnabled(false);
        secondOption.setEnabled(false);
        thirdOption.setEnabled(false);
        fourthOption.setEnabled(false);

        firstOption.setTextColor(Color.WHITE);
        secondOption.setTextColor(Color.WHITE);
        thirdOption.setTextColor(Color.WHITE);
        fourthOption.setTextColor(Color.WHITE);
    }

    private void disableCheckboxes() {
        firstCheckbox.setEnabled(false);
        secondCheckbox.setEnabled(false);
        thirdCheckbox.setEnabled(false);
        fourthCheckbox.setEnabled(false);
    }

    private void resetButtonColors() {
        ColorStateList defaultColor = ContextCompat.getColorStateList(this, R.color.default_purple);

        firstOption.setBackgroundTintList(defaultColor);
        secondOption.setBackgroundTintList(defaultColor);
        thirdOption.setBackgroundTintList(defaultColor);
        fourthOption.setBackgroundTintList(defaultColor);

        firstOption.setEnabled(true);
        secondOption.setEnabled(true);
        thirdOption.setEnabled(true);
        fourthOption.setEnabled(true);

        firstOption.setTextColor(Color.WHITE);
        secondOption.setTextColor(Color.WHITE);
        thirdOption.setTextColor(Color.WHITE);
        fourthOption.setTextColor(Color.WHITE);
    }

    private void resetCheckboxColors() {

        GradientDrawable dottedBorder = (GradientDrawable) ContextCompat.getDrawable(this, R.drawable.dotted_border);

        if (dottedBorder != null) {
            dottedBorder.setStroke(2, ContextCompat.getColor(this, R.color.default_purple), 4, 2);
        }

        firstCheckbox.setBackground(dottedBorder);
        secondCheckbox.setBackground(dottedBorder);
        thirdCheckbox.setBackground(dottedBorder);
        fourthCheckbox.setBackground(dottedBorder);

        firstCheckbox.setTextColor(ContextCompat.getColor(this, R.color.black));
        secondCheckbox.setTextColor(ContextCompat.getColor(this, R.color.black));
        thirdCheckbox.setTextColor(ContextCompat.getColor(this, R.color.black));
        fourthCheckbox.setTextColor(ContextCompat.getColor(this, R.color.black));

        firstCheckbox.setEnabled(true);
        secondCheckbox.setEnabled(true);
        thirdCheckbox.setEnabled(true);
        fourthCheckbox.setEnabled(true);

        resetCheckboxes();
    }

    private void startTimer(int duration) {

        if (timer != null) {
            timer.cancel();
        }

        timer = new CountDownTimer(duration * 1000L, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeft = millisUntilFinished;
                updateTimer();
            }

            @Override
            public void onFinish() {
                Toast.makeText(QuizActivity.this, "Time's up!", Toast.LENGTH_SHORT).show();
                loadNextQuestion();
            }
        }.start();
    }

    private void updateTimer() {
        int seconds = (int) (timeLeft / 1000) % 60;
        int minutes = (int) ((timeLeft / (1000 * 60)) % 60);

        String timeFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        timerView.setText(timeFormatted);
    }

    private void checkAnswer(Question question) {
        List<Integer> selectedAnswers = new ArrayList<>();
        List<Integer> correctAnswers = question.getCorrectAnswers();

        if (Objects.equals(question.getType(), "multiple choice")) {

            if (firstCheckbox.isChecked()) selectedAnswers.add(0);
            if (secondCheckbox.isChecked()) selectedAnswers.add(1);
            if (thirdCheckbox.isChecked()) selectedAnswers.add(2);
            if (fourthCheckbox.isChecked()) selectedAnswers.add(3);

            for (int index : correctAnswers) {
                highlightCheckbox(index, R.color.green);
            }

            for (int index : selectedAnswers) {
                if (!correctAnswers.contains(index)) {
                    highlightCheckbox(index, R.color.red);
                }
            }

            Collections.sort(selectedAnswers);
            Collections.sort(correctAnswers);

            if (selectedAnswers.equals(correctAnswers)) {
                score += 1;
                scoreView.setText(String.valueOf(score));
                Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Incorrect!", Toast.LENGTH_SHORT).show();
                incorrectQuestions.add(question);
            }

            disableCheckboxes();

        } else if (Objects.equals(question.getType(), "single")) {
            if (selectedOptionIndex == -1) {
                Toast.makeText(this, "Please select an option.", Toast.LENGTH_SHORT).show();
                return;
            }

            int correctAnswerIndex = correctAnswers.get(0);
            highlightButton(correctAnswerIndex, Color.GREEN);

            if (selectedOptionIndex == correctAnswerIndex) {
                Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show();
                score += 1;
                scoreView.setText(String.valueOf(score));
            } else {
                highlightButton(selectedOptionIndex, Color.RED);
                Toast.makeText(this, "Incorrect!", Toast.LENGTH_SHORT).show();
                incorrectQuestions.add(question);
            }

            disableButtons();
            selectedOptionIndex = -1;
        }

        if (timer != null) {
            timer.cancel();
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loadNextQuestion();
            }
        }, 2000);

    }

    @Override
    protected void onDestroy() {
        if (timer != null) {
            timer.cancel();
        }
        super.onDestroy();
    }
}
