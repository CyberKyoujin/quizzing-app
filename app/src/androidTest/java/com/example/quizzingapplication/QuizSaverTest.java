package com.example.quizzingapplication;

import static com.google.common.truth.Truth.assertThat;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

public class QuizSaverTest {

    private File quizFile;
    private QuizSaver quizSaver;

    @Before
    public void setUp() throws Exception {
        Context context = ApplicationProvider.getApplicationContext();
        quizFile = new File(context.getFilesDir(), "test_quizzes.json");

        if (!quizFile.exists()) {
            quizFile.createNewFile();
        }

        JSONArray emptyArray = new JSONArray();
        try (FileOutputStream fos = new FileOutputStream(quizFile)) {
            fos.write(emptyArray.toString().getBytes(StandardCharsets.UTF_8));
        }

        quizSaver = new QuizSaver();
    }


    // Test save quiz
    @Test
    public void testSaveQuiz() throws Exception {
        List<String> options = Arrays.asList("Yes", "No");
        List<Integer> answers = Arrays.asList(0);
        Question q = new Question("Test question?", "single", options, answers);
        Quiz quiz = new Quiz("Test Quiz", Arrays.asList(q), 30);

        boolean result = quizSaver.saveQuiz(quiz, quizFile);
        assertThat(result).isTrue();

        FileInputStream fis = new FileInputStream(quizFile);
        byte[] buffer = new byte[(int) quizFile.length()];
        fis.read(buffer);
        fis.close();

        String json = new String(buffer, "UTF-8");
        JSONArray quizArray = new JSONArray(json);

        assertThat(quizArray.length()).isEqualTo(1);
        JSONObject quizObj = quizArray.getJSONObject(0);
        assertThat(quizObj.getString("name")).isEqualTo("Test Quiz");
        assertThat(quizObj.getInt("timer_duration")).isEqualTo(30);
    }
}
