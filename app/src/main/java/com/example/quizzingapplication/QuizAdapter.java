package com.example.quizzingapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizzingapplication.Quiz;
import com.example.quizzingapplication.R;

import java.util.List;

public class QuizAdapter extends RecyclerView.Adapter<QuizAdapter.QuizViewHolder> {

    private List<Quiz> quizList;
    private Context context;
    private OnQuizClickListener listener;

    public interface OnQuizClickListener {
        void onQuizClick(Quiz quiz);
    }

    public QuizAdapter(Context context, List<Quiz> quizList, OnQuizClickListener listener) {
        this.context = context;
        this.quizList = quizList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public QuizViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.quiz_item, parent, false);
        return new QuizViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuizViewHolder holder, int position) {
        Quiz quiz = quizList.get(position);
        holder.quizName.setText(quiz.getName());
        holder.startQuizButton.setOnClickListener(v -> listener.onQuizClick(quiz));
    }

    @Override
    public int getItemCount() {
        return quizList.size();
    }

    public static class QuizViewHolder extends RecyclerView.ViewHolder {
        TextView quizName;
        Button startQuizButton;

        public QuizViewHolder(@NonNull View itemView) {
            super(itemView);
            quizName = itemView.findViewById(R.id.quiz_name);
            startQuizButton = itemView.findViewById(R.id.start_quiz_button);
        }
    }
}

