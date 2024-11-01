package com.example.quizzingapplication;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class QuizActivity extends AppCompatActivity {

    private List<Question> questionList;

    private TextView questionView;
    private TextView scoreView;
    private TextView questionTypeView;
    private TextView repeatQuestionsView;

    private Button firstOption;
    private Button secondOption;
    private Button thirdOption;
    private Button fourthOption;

    private CheckBox firstCheckbox;
    private CheckBox secondCheckbox;
    private CheckBox thirdCheckbox;
    private CheckBox fourthCheckbox;

    private Button submitBtn;

    private int currentQuestionIndex = 0;
    private int score = 0;

    private List<Question> incorrectQuestions = new ArrayList<>();
    private int repeatCount = 0;
    private final int MAX_REPEATS = 1;

    private int selectedOptionIndex = -1;

    private Question currentQuestion;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_activity);

        questionList = loadQuestions();

        questionView = findViewById(R.id.text_question);
        scoreView = findViewById(R.id.score_view);
        questionTypeView = findViewById(R.id.question_type_view);
        repeatQuestionsView = findViewById(R.id.repeat_questions_view);

        repeatQuestionsView.setVisibility(View.GONE);

        firstOption = findViewById(R.id.button_option1);
        secondOption = findViewById(R.id.button_option2);
        thirdOption = findViewById(R.id.button_option3);
        fourthOption = findViewById(R.id.button_option4);

        firstCheckbox = findViewById(R.id.checkbox_option1);
        secondCheckbox = findViewById(R.id.checkbox_option2);
        thirdCheckbox = findViewById(R.id.checkbox_option3);
        fourthCheckbox = findViewById(R.id.checkbox_option4);

        submitBtn = findViewById(R.id.submit_btn);

        if (!questionList.isEmpty()) {
            currentQuestion = questionList.get(currentQuestionIndex);
            displayQuestion(currentQuestion);
        } else {
            Toast.makeText(this, "No questions available", Toast.LENGTH_LONG).show();
        }

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer(currentQuestion);
            }
        });

        scoreView.setText(String.valueOf(score));
    }

    private List<Question> loadQuestions() {
        List<Question> questionList = new ArrayList<>();

        try{
            InputStream is = getAssets().open("questions.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            String json = new String(buffer, StandardCharsets.UTF_8);
            JSONArray jsonArray = new JSONArray(json);

            for (int i = 0; i < jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                String text = jsonObject.getString("text");
                String type = jsonObject.getString("type");

                JSONArray optionsArray = jsonObject.getJSONArray("options");
                List<String> options = new ArrayList<>();
                for (int j = 0; j < optionsArray.length(); j++){
                    options.add(optionsArray.getString(j));
                }

                JSONArray answersArray = jsonObject.getJSONArray("answers");
                List<Integer> correctAnswers = new ArrayList<>();
                for (int j = 0; j < answersArray.length(); j++) {
                    correctAnswers.add(answersArray.getInt(j));
                }

                Question question = new Question(text, type, options, correctAnswers);
                questionList.add(question);

            }

        } catch (IOException | JSONException e){
            e.printStackTrace();
            Toast.makeText(this, "Error loading questions", Toast.LENGTH_LONG).show();
        }

        return questionList;
    }

    @SuppressLint("SetTextI18n")
    private void displayQuestion(Question question) {

        resetVisibility();
        resetButtonColors();
        resetCheckboxColors();

        // Set the question text
        questionView.setText(question.getText());
        List<String> options = question.getOptions();
        List<Integer> answers = question.getCorrectAnswers();
        String questionType = question.getType();

        if (Objects.equals(questionType, "multiple choice")) {
            // Multiple-answer question: checkboxes
            setCheckboxes(options);
            questionTypeView.setText("Multiple - " + answers.size() + " correct answers");
            submitBtn.setVisibility(View.VISIBLE);
        } else if (Objects.equals(questionType, "single")) {
            // Single-answer question: buttons
            setButtons(options);
            questionTypeView.setText("Single");
        }

    }

    private void loadNextQuestion() {
        currentQuestionIndex++;

        resetVisibility();
        resetButtonColors();
        resetCheckboxColors();

        if (currentQuestionIndex < questionList.size()) {
            currentQuestion = questionList.get(currentQuestionIndex);
            displayQuestion(currentQuestion);
        } else if (!incorrectQuestions.isEmpty() && repeatCount < MAX_REPEATS) {
            questionList = new ArrayList<>(incorrectQuestions);
            incorrectQuestions.clear();
            currentQuestionIndex = 0;
            currentQuestion = questionList.get(currentQuestionIndex);
            repeatCount++;
            Toast.makeText(this, "Repeating incorrect questions", Toast.LENGTH_LONG).show();
            displayQuestion(currentQuestion);
            repeatQuestionsView.setVisibility(View.VISIBLE);
        } else {
            Toast.makeText(this, "Quiz Finished!", Toast.LENGTH_LONG).show();
        }
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
        repeatQuestionsView.setVisibility(View.GONE);
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

    private void resetButtons() {
        selectedOptionIndex = -1;
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
        }
    }

    private void highlightCheckbox(int index, int color) {

        GradientDrawable dottedBorder = (GradientDrawable) ContextCompat.getDrawable(this, R.drawable.dotted_border);

        if (dottedBorder != null) {
            dottedBorder.setStroke(2, color, 4, 2);
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
    }


    private void resetCheckboxColors() {

        GradientDrawable dottedBorder = (GradientDrawable) ContextCompat.getDrawable(this, R.drawable.dotted_border);

        if (dottedBorder != null) {
            dottedBorder.setStroke(2, R.color.default_purple, 4, 2);
        }

        firstCheckbox.setBackground(dottedBorder);
        secondCheckbox.setBackground(dottedBorder);
        thirdCheckbox.setBackground(dottedBorder);
        fourthCheckbox.setBackground(dottedBorder);

        firstCheckbox.setEnabled(true);
        secondCheckbox.setEnabled(true);
        thirdCheckbox.setEnabled(true);
        fourthCheckbox.setEnabled(true);

        resetCheckboxes();
    }

    private void checkAnswer(Question question) {
        List<Integer> selectedAnswers = new ArrayList<>();
        List<Integer> correctAnswers = question.getCorrectAnswers();
        int correctAnswerIndex = correctAnswers.get(0);

        if (Objects.equals(question.getType(), "multiple choice")) {

            if (firstCheckbox.isChecked()) selectedAnswers.add(0);
            if (secondCheckbox.isChecked()) selectedAnswers.add(1);
            if (thirdCheckbox.isChecked()) selectedAnswers.add(2);
            if (fourthCheckbox.isChecked()) selectedAnswers.add(3);

            for (int index : correctAnswers) {
                highlightCheckbox(index, Color.GREEN);
            }

            for (int index : selectedAnswers) {
                if (!correctAnswers.contains(index)) {
                    highlightCheckbox(index, Color.RED);
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
            highlightButton(correctAnswerIndex, Color.GREEN);

            if (selectedOptionIndex == correctAnswers.get(0)) {
                Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show();
                score += 1;
                scoreView.setText(String.valueOf(score));
            } else {
                highlightButton(selectedOptionIndex, Color.RED);
                Toast.makeText(this, "Incorrect!", Toast.LENGTH_SHORT).show();
                incorrectQuestions.add(currentQuestion);
            }

            selectedOptionIndex = -1;
            disableButtons();
        }

        new Handler().postDelayed(this::loadNextQuestion, 2000);
    }

}
