package com.example.quizgame;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.quizgame.question.Question;
import com.example.quizgame.question.QuestionViewModel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class QuestionActivity extends AppCompatActivity {
    //Declare quiz question variables
    public static final String EXTRA_SCORE = "extraScore";
    private List<Question> questionsL;
    private QuestionViewModel questionViewModel;
    private int counter;
    private int score;
    private boolean answered;
    private CountDownTimer CountdownClock;
    private long timeLeft;
    private TextView viewScore;
    private TextView tvCountdown;
    private RadioGroup rbGroup;
    private Button nextQuestion;
    private Button home;
    private TextView tx1;
    private TextView tx2;
    private TextView tx3;
    private RadioButton rb1;
    private RadioButton rb2;
    private RadioButton rb3;
    private RadioButton rb4;
    private RadioButton selected;
    private Spinner difficulty;
    private ColorStateList textColorDefault;
    MediaPlayer backgroundmusic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        //Generate background music
        backgroundmusic = MediaPlayer.create(getApplicationContext(), R.raw.background_music);
        backgroundmusic.start();

        rb1 = findViewById(R.id.btnOption1);
        rb2 = findViewById(R.id.btnOption2);
        rb3 = findViewById(R.id.btnOption3);
        rb4 = findViewById(R.id.btnOption4);
        home = findViewById(R.id.btnQuestionHome);
        viewScore = findViewById(R.id.tvScore);
        nextQuestion = findViewById(R.id.btnNext);
        tvCountdown = findViewById(R.id.tvTimer);
        rbGroup = findViewById(R.id.radioGroup);
        textColorDefault = rb1.getTextColors();
        difficulty = findViewById(R.id.Difficulty_Spinner);

        //Gather questions from room database
        questionViewModel = new ViewModelProvider(this).get(QuestionViewModel.class);
        String typeA = getIntent().getStringExtra("type").toLowerCase();
        questionViewModel.getAllQuestions().observe(this, questions -> {
            ArrayList<Question> tmQ = new ArrayList<>();
            //System.out.println(questions.size() + " UUUUUUUUUUUUUUUUUUUUUUUU");
            for (Question q : questions) {
                if (q.type.equals(typeA)) {
                    tmQ.add(q);
                }
            }
            HashSet<Question> set = new HashSet<>();
            Random ed = new Random();
            while (set.size() < 10) {
                Question a = tmQ.get(ed.nextInt(tmQ.size()));
                set.add(a);
            }
            questionsL = new ArrayList<>();
            questionsL.addAll(set);
            counter = 0;
            activateQuestion(counter);
        });

        //Save Instance State
        if(savedInstanceState == null) {
            counter = 0;
            questionsL = new ArrayList<>();
        }
        else{
            questionsL = savedInstanceState.getParcelable("questions");
            counter = savedInstanceState.getInt("counter");
            score = savedInstanceState.getInt("score");
            timeLeft = savedInstanceState.getLong("timeLeft");
            answered = savedInstanceState.getBoolean("answered");
        }

        //Make the user answer question
        nextQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!answered) {
                    if (rb1.isChecked() || rb2.isChecked() || rb3.isChecked() || rb4.isChecked()) {
                        CheckAnswer();
                    } else {
                        Toast.makeText(QuestionActivity.this, "Please choose an answer!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    nextQuestion();
                }
            }
        });

        //Show alert dialog that gives user option to quit quiz
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(QuestionActivity.this);
                builder.setMessage("Are you sure you want to quit?");
                builder.setCancelable(true);

                builder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });
                builder.setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
            });
    }

    @Override
    protected void onPause() {
        super.onPause();
        backgroundmusic.stop();
        backgroundmusic.release();
    }

    //Show question content on activity screen
    private void activateQuestion(int counter) {
        rbGroup.clearCheck();
        rb1.setTextColor(textColorDefault);
        rb2.setTextColor(textColorDefault);
        rb3.setTextColor(textColorDefault);
        rb4.setTextColor(textColorDefault);

        if (counter < 10) {
            tx1 = findViewById(R.id.tvQuestion_Number);
            tx1.setText(String.format("Question %d", counter + 1));
            tx2 = findViewById(R.id.tvQuestion_Content);
            tx2.setText(questionsL.get(counter).question);
            tx3 = findViewById(R.id.tvQuestion_Content);
            tx3.setText(questionsL.get(counter).question);
            rb1.setText(questionsL.get(counter).opt1);
            rb2.setText(questionsL.get(counter).opt2);
            rb3.setText(questionsL.get(counter).opt3);
            rb4.setText(questionsL.get(counter).opt4);
            timeLeft = 90000;
            StartCountDown();
        } else {
            finishQuiz();
        }
    }

    //Navigate between pages
    public void nextQuestion() {
        counter++;
        answered = false;
        nextQuestion.setText("Submit");
        activateQuestion(counter);
    }

    //Activate countdown clock
    private void StartCountDown() {
        CountdownClock = new CountDownTimer(timeLeft, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeft = millisUntilFinished;
                UpdateCountDownText();
            }

            @Override
            public void onFinish() {
                timeLeft = 0;
                UpdateCountDownText();
                CheckAnswer();
            }
        }.start();
    }

    private void UpdateCountDownText() {
        int minutes = (int) (timeLeft / 1000) / 60;
        int seconds = (int) (timeLeft / 1000) % 60;
        String timeFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        tvCountdown.setText(timeFormatted);
        if (timeLeft < 10000) {
            tvCountdown.setTextColor(Color.RED);
        } else {
            tvCountdown.setTextColor(Color.GREEN);
        }
    }

    //Check user answer
    private void CheckAnswer() {
        answered = true;
        CountdownClock.cancel(); //stop clock after question is answered
        selected = findViewById(rbGroup.getCheckedRadioButtonId());

        //10 points for correct answer, loss of 10 points for incorrect answer
        if (selected.getText().equals(questionsL.get(counter).answ)) {
            score = score + 10;
        } else {
            score = score - 10;
        }
        ShowSolution();
        viewScore.setText("Score: " + score);
    }

    //Show correct answer
    private void ShowSolution() {
        rb1.setTextColor(Color.RED);
        rb2.setTextColor(Color.RED);
        rb3.setTextColor(Color.RED);
        rb4.setTextColor(Color.RED);

        //Highlight correct answer
        if (questionsL.get(counter).opt1.equals(questionsL.get(counter).answ) ) {
            rb1.setTextColor(Color.GREEN);
            tx2.setText("Choice 1 is correct");
        }
        else if (questionsL.get(counter).opt2.equals(questionsL.get(counter).answ)) {
            rb2.setTextColor(Color.GREEN);
            tx2.setText("Choice 2 is correct");
        }
        else if (questionsL.get(counter).opt3.equals(questionsL.get(counter).answ)) {
            rb3.setTextColor(Color.GREEN);
            tx2.setText("Choice 3 is correct");
        }
        else{
            rb4.setTextColor(Color.GREEN);
            tx2.setText("Choice 4 is correct");
        }

        //Toggle button text between next and complete while answering questions
        if (counter < 10) {
            nextQuestion.setText("Next");
        }
        else {
           nextQuestion.setText("Complete");
        }
    }

    //Complete Quiz
    private void finishQuiz() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(EXTRA_SCORE, score);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    //Terminate countdown clock
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (CountdownClock != null) {
            CountdownClock.cancel();
        }
    }

    //Save Instance State
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("counter", counter);
        outState.putInt("score", score);
        outState.putLong("timeleft", timeLeft);
        outState.putBoolean("answered", answered);
        outState.putParcelable("questions", (Parcelable) questionsL);
    }
}