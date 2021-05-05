package com.example.quizgame.question;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface QuestionDAO {
    @Query("SELECT * FROM questions WHERE typeq = :type " + "ORDER BY question COLLATE NOCASE, rowid")
    LiveData<List<Question>> getCurrent(String type);

    @Query("SELECT * FROM questions ORDER BY question COLLATE NOCASE, rowid")
    LiveData<List<Question>> getAll();

    @Query("SELECT * FROM questions WHERE rowid = :questionId")
    Question getById(int questionId);

    @Insert
    void insert(Question... questions);

    @Query("DELETE FROM questions WHERE rowid = :questionId")
    void delete(int questionId);
}

