package com.example.quizgame.question;
import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class QuestionViewModel extends AndroidViewModel {
    private LiveData<List<Question>> questions;

    public QuestionViewModel (Application application) {
        super(application);
        questions = QuestionDatabase.getDatabase(getApplication()).questionDAO().getAll();
    }

    public void filterQuestions(String  type) {
        if (type!=null)
            questions = QuestionDatabase.getDatabase(getApplication()).questionDAO().getCurrent(type);
        else
            questions = QuestionDatabase.getDatabase(getApplication()).questionDAO().getAll();
    }

    public LiveData<List<Question>> getAllQuestions() {
        return questions;
    }
}