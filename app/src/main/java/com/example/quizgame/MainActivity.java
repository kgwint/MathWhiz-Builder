package com.example.quizgame;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    //Variables to save high score
    private static final int REQUEST_CODE_QUIZ = 10;
    public static final String SHARED_PREF = "SharedPref";
    public static final String KEY_EASY_HIGH_SCORE = "keyEasyHighScore";
    public static final String KEY_MEDIUM_HIGH_SCORE = "keyMediumHighScore";
    public static final String KEY_HARD_HIGH_SCORE = "keyHardHighScore";
    private int EasyHighScore;
    private int MediumHighScore;
    private int HardHighScore;
    private TextView tvEasyHighScore;
    private TextView tvMediumHighScore;
    private TextView tvHardHighScore;
    private Spinner difficulty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        difficulty = findViewById(R.id.Difficulty_Spinner);
        tvEasyHighScore = findViewById(R.id.tvEasyHighScore);
        tvMediumHighScore = findViewById(R.id.tvMediumHighScore);
        tvHardHighScore = findViewById(R.id.tvHardHighScore);
        LoadHighScore(); //show highscore when start screen is loaded
    }

    //Navigate between pages
    public void takeQuiz(View view) {
        Spinner data=findViewById(R.id.Difficulty_Spinner);
        String choice=data.getSelectedItem().toString();
        Intent intent = new Intent(this, QuestionActivity.class);
        intent.putExtra("type",choice);
        startActivityForResult(intent, REQUEST_CODE_QUIZ);
    }

    public void viewRules(View view) {
        Intent intent = new Intent(this, Rules.class);
        startActivity(intent);
    }

    //Use shared preferences to save high score
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE_QUIZ) {
            if (resultCode == RESULT_OK) {
                int score = data.getIntExtra(QuestionActivity.EXTRA_SCORE, 0);

                //Only update the high score for a particular difficulty
                if (score > EasyHighScore || score > MediumHighScore || score > HardHighScore) {
                    UpdateHighScore(score);
                }
            }
        }
    }

    private void LoadHighScore() {
        SharedPreferences pref = getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
        EasyHighScore = pref.getInt(KEY_EASY_HIGH_SCORE, 0);
        MediumHighScore = pref.getInt(KEY_MEDIUM_HIGH_SCORE, 0);
        HardHighScore = pref.getInt(KEY_HARD_HIGH_SCORE, 0);

        tvEasyHighScore.setText("Easy: " + EasyHighScore);
        tvMediumHighScore.setText("Medium: " + MediumHighScore);
        tvHardHighScore.setText("Hard: " + HardHighScore);
    }

    private void UpdateHighScore(int NewHighScore) {
        String level = difficulty.getSelectedItem().toString();

        if(level.equals("Easy")) {
            EasyHighScore = NewHighScore;
            tvEasyHighScore.setText("Easy: " + EasyHighScore);
        }
        else if(level.equals("Medium")) {
            MediumHighScore = NewHighScore;
            tvMediumHighScore.setText("Medium: " + MediumHighScore);
        }
        else {
            HardHighScore = NewHighScore;
            tvHardHighScore.setText("Hard: " + HardHighScore);
        }

        SharedPreferences pref = getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(KEY_EASY_HIGH_SCORE, EasyHighScore);
        editor.putInt(KEY_MEDIUM_HIGH_SCORE, MediumHighScore);
        editor.putInt(KEY_HARD_HIGH_SCORE, HardHighScore);
        editor.apply();
    }

    //Save Instance State
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("easyhighScore", EasyHighScore);
        outState.putInt("mediumhighScore", MediumHighScore);
        outState.putInt("hardhighScore", HardHighScore);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedState) {
        super.onRestoreInstanceState(savedState);
        EasyHighScore = savedState.getInt("easyhighScore");
        MediumHighScore = savedState.getInt("mediumhighScore");
        HardHighScore = savedState.getInt("hardhighScore");
    }
}


