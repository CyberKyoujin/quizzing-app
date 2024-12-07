package com.example.quizzingapplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class QuizSaver {

    public boolean saveQuiz(Quiz newQuiz, File file) {

        try {

            // Load existing quizzes
            FileInputStream fis = new FileInputStream(file);
            byte[] buffer = new byte[(int) file.length()];
            fis.read(buffer);
            fis.close();

            String json = new String(buffer, "UTF-8");
            JSONArray quizArray = new JSONArray(json);

            // Converting quiz to JSON Object
            JSONObject newQuizObject = new JSONObject();
            newQuizObject.put("name", newQuiz.getName());
            newQuizObject.put("timer_duration", newQuiz.getTimerDuration());

            JSONArray questionsArray = new JSONArray();
            for (Question q : newQuiz.getQuestions()) {
                JSONObject questionObject = new JSONObject();
                questionObject.put("text", q.getText());
                questionObject.put("type", q.getType());

                JSONArray optionsJsonArray = new JSONArray(q.getOptions());
                questionObject.put("options", optionsJsonArray);

                JSONArray answersJsonArray = new JSONArray(q.getCorrectAnswers());
                questionObject.put("answers", answersJsonArray);

                questionsArray.put(questionObject);
            }

            newQuizObject.put("questions", questionsArray);
            quizArray.put(newQuizObject);

            // Write new quiz to the JSON file
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(quizArray.toString(4).getBytes("UTF-8"));
            fos.close();

            return true;

        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return false;

        }
    }
}
