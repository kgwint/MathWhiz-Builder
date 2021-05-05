package com.example.quizgame.question;
import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Question.class}, version = 1, exportSchema = false)
public abstract class QuestionDatabase extends RoomDatabase {
    public interface QuestionListener {
        void onQuestionReturned(Question question);
    }

    public abstract QuestionDAO questionDAO();

    private static QuestionDatabase INSTANCE;

    public static QuestionDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (QuestionDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), QuestionDatabase.class, "question_database").addCallback(createQuestionDatabaseCallback).build();
                }
            }
        }
        return INSTANCE;
    }

    // Note this call back will be run
    private static RoomDatabase.Callback createQuestionDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            createQuestionTable();
        }
    };

    private static void createQuestionTable() {
        for (int i = 0; i < DataBase_Values.Easy_Questions.length; i++) {
            insert(new Question(0, DataBase_Values.Easy_Questions[i], "easy", DataBase_Values.Easy_Option1[i], DataBase_Values.Easy_Option2[i], DataBase_Values.Easy_Option3[i], DataBase_Values.Easy_Option4[i], DataBase_Values.Easy_Correct[i]));
        }
        System.out.println("now medium");
        System.out.println(DataBase_Values.Medium_Questions.length);
        System.out.println(DataBase_Values.Medium_Option1.length);
        System.out.println(DataBase_Values.Medium_Option2.length);
        System.out.println(DataBase_Values.Medium_Option3.length);
        System.out.println(DataBase_Values.Medium_Option4.length);
        System.out.println(DataBase_Values.Medium_Correct.length);
        System.out.println("now medium");
        for (int i = 0; i < DataBase_Values.Medium_Questions.length; i++) {
            insert(new Question(0, DataBase_Values.Medium_Questions[i], "medium", DataBase_Values.Medium_Option1[i], DataBase_Values.Medium_Option2[i], DataBase_Values.Medium_Option3[i], DataBase_Values.Medium_Option4[i], DataBase_Values.Medium_Correct[i]));
        }
        System.out.println("now hard");
        System.out.println(DataBase_Values.Hard_Questions.length);
        System.out.println(DataBase_Values.Hard_Option1.length);
        System.out.println(DataBase_Values.Hard_Option2.length);
        System.out.println(DataBase_Values.Hard_Option3.length);
        System.out.println(DataBase_Values.Hard_Option4.length);
        System.out.println(DataBase_Values.Hard_Correct.length);
        System.out.println("now hard");
        for (int i = 0; i < DataBase_Values.Hard_Questions.length; i++) {
            insert(new Question(0, DataBase_Values.Hard_Questions[i], "hard", DataBase_Values.Hard_Option1[i], DataBase_Values.Hard_Option2[i], DataBase_Values.Hard_Option3[i], DataBase_Values.Hard_Option4[i], DataBase_Values.Hard_Correct[i]));
        }
        System.out.println("now all");
    }

    public static void getQuestion(int id, QuestionListener listener) {
        new AsyncTask<Integer, Void, Question>() {
            protected Question doInBackground(Integer... ids) {
                return INSTANCE.questionDAO().getById(ids[0]);
            }

            protected void onPostExecute(Question question) {
                super.onPostExecute(question);
                listener.onQuestionReturned(question);
            }
        }.execute(id);
    }

    public static void insert(Question question) {
        new AsyncTask<Question, Void, Void>() {
            protected Void doInBackground(Question... questions) {
                INSTANCE.questionDAO().insert(questions);
                return null;
            }
        }.execute(question);
    }
}