package com.muilat.android.offlinetutorial;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.muilat.android.offlinetutorial.data.OfflineTutorialContract;
import com.muilat.android.offlinetutorial.data.Quiz;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;
import java.util.Random;

public class QuizActivity extends AppCompatActivity {
    public static final String EXTRA_SCORE = "extraScore";
    private static final long COUNTDOWN_IN_MILLIS = 30000;

    private static final String KEY_SCORE = "keyScore";
    private static final String KEY_QUESTION_COUNT = "keyQuizCount";
    private static final String KEY_MILLIS_LEFT = "keyMillisLeft";
    private static final String KEY_ANSWERED = "keyAnswered";
    private static final String KEY_QUESTION_LIST = "keyQuizList";

    private TextView textViewQuestion;
    private TextView textViewScore;
    private TextView textViewQuizCount;
    private TextView textViewCountDown;
    private TextView textViewAnswerResult;
    private RadioGroup rbGroup;
    private RadioButton rb1;
    private RadioButton rb2;
    private RadioButton rb3;
    private RadioButton rb4;
    private Button buttonConfirmNext;

    private ColorStateList textColorDefaultRb;
    private ColorStateList textColorDefaultCd;

    private CountDownTimer countDownTimer;
    private long timeLeftInMillis;

    private ArrayList<Quiz> quizList = new ArrayList<>();
    private int quizCounter;
    private int quizCountTotal;
    private Quiz currentQuiz;

    private int score;
    private boolean answered;

    private long backPressedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        textViewQuestion = findViewById(R.id.text_view_quiz);
        textViewScore = findViewById(R.id.text_view_score);
        textViewQuizCount = findViewById(R.id.text_view_quiz_count);
        textViewCountDown = findViewById(R.id.text_view_countdown);
        textViewAnswerResult = findViewById(R.id.answer_result);

        rbGroup = findViewById(R.id.radio_group);
        rb1 = findViewById(R.id.radio_button1);
        rb2 = findViewById(R.id.radio_button2);
        rb3 = findViewById(R.id.radio_button3);
        rb4 = findViewById(R.id.radio_button4);
        buttonConfirmNext = findViewById(R.id.button_confirm_next);

        textColorDefaultRb = rb1.getTextColors();
        textColorDefaultCd = textViewCountDown.getTextColors();

        if (savedInstanceState == null) {
            Cursor quizCursor = getContentResolver().query(OfflineTutorialContract.QuizEntry.CONTENT_URI,
                    null,
                    null,
                    null,
                    null);

//            while(quizCursor.moveToNext()){
//                quizList.add(new Quiz(quizCursor)) ;
//            }

            float pref_quiz_total;

            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            pref_quiz_total = Float.parseFloat(sharedPreferences.getString(getString(R.string.pref_quiz_no_key),
                    getString(R.string.pref_quiz_no_default)));

            if(pref_quiz_total>quizCursor.getCount())
                pref_quiz_total = quizCursor.getCount();

            for(int i = 0; i<pref_quiz_total; i++){
                quizCursor.moveToPosition(new Random().nextInt(quizCursor.getCount()));
                quizList.add(new Quiz(quizCursor)) ;

            }

            quizCountTotal = quizList.size();
            Collections.shuffle(quizList);

            showNextQuiz();
        } else {
            quizList = savedInstanceState.getParcelableArrayList(KEY_QUESTION_LIST);
            quizCountTotal = quizList.size();
            quizCounter = savedInstanceState.getInt(KEY_QUESTION_COUNT);
            currentQuiz = quizList.get(quizCounter - 1);
            score = savedInstanceState.getInt(KEY_SCORE);
            timeLeftInMillis = savedInstanceState.getLong(KEY_MILLIS_LEFT);
            answered = savedInstanceState.getBoolean(KEY_ANSWERED);

            if (!answered) {
                startCountDown();
            } else {
                updateCountDownText();
                showSolution();
            }
        }

        buttonConfirmNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!answered) {
                    if (rb1.isChecked() || rb2.isChecked() || rb3.isChecked() || rb4.isChecked()) {
                        checkAnswer();
                    } else {
                        Toast.makeText(QuizActivity.this, "Please select an answer", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    showNextQuiz();
                }
            }
        });
    }

    private void showNextQuiz() {
        rb1.setTextColor(textColorDefaultRb);
        rb2.setTextColor(textColorDefaultRb);
        rb3.setTextColor(textColorDefaultRb);
        rb4.setTextColor(textColorDefaultRb);
        rbGroup.clearCheck();
        textViewAnswerResult.setVisibility(View.GONE);

        if (quizCounter < quizCountTotal) {
            currentQuiz = quizList.get(quizCounter);

            textViewQuestion.setText(currentQuiz.getQuestion());
            rb1.setText(currentQuiz.getOption1());
            rb2.setText(currentQuiz.getOption2());
            rb3.setText(currentQuiz.getOption3());
            rb4.setText(currentQuiz.getOption4());

            quizCounter++;
            textViewQuizCount.setText("Quiz: " + quizCounter + "/" + quizCountTotal);
            answered = false;
            buttonConfirmNext.setText("Confirm");

            timeLeftInMillis = COUNTDOWN_IN_MILLIS;
            startCountDown();
        } else {
            finishQuiz();
        }
    }

    private void startCountDown() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                timeLeftInMillis = 0;
                updateCountDownText();
                checkAnswer();
            }
        }.start();
    }

    private void updateCountDownText() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;

        String timeFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

        textViewCountDown.setText(timeFormatted);

        if (timeLeftInMillis < 10000) {
            textViewCountDown.setTextColor(Color.RED);
        } else {
            textViewCountDown.setTextColor(textColorDefaultCd);
        }
    }

    private void checkAnswer() {
        answered = true;

        countDownTimer.cancel();

        RadioButton rbSelected = findViewById(rbGroup.getCheckedRadioButtonId());
        int answerNr = rbGroup.indexOfChild(rbSelected) + 1;
        textViewAnswerResult.setVisibility(View.VISIBLE);

        if (answerNr == currentQuiz.getAnswerNr()) {
            score++;
            textViewScore.setText("Score: " + score);

            textViewAnswerResult.setText("You are correct");
            textViewAnswerResult.setTextColor(getResources().getColor(R.color.colorCorrect));
        }
        else {
            textViewAnswerResult.setText("You are wrong");
            textViewAnswerResult.setTextColor(getResources().getColor(R.color.colorWrong));

        }

        showSolution();
    }

    private void showSolution() {
        rb1.setTextColor(getResources().getColor(R.color.colorWrong));
        rb2.setTextColor(getResources().getColor(R.color.colorWrong));
        rb3.setTextColor(getResources().getColor(R.color.colorWrong));
        rb4.setTextColor(getResources().getColor(R.color.colorWrong));

        switch (currentQuiz.getAnswerNr()) {
            case 1:
                rb1.setTextColor(getResources().getColor(R.color.colorCorrect));
                break;
            case 2:
                rb2.setTextColor(getResources().getColor(R.color.colorCorrect));
                break;
            case 3:
                rb3.setTextColor(getResources().getColor(R.color.colorCorrect));
                break;
                case 4:
                rb3.setTextColor(getResources().getColor(R.color.colorCorrect));
                break;
        }

        if (quizCounter < quizCountTotal) {
            buttonConfirmNext.setText("Next");
        } else {
            buttonConfirmNext.setText("Finish");
        }
    }

    private void finishQuiz() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(EXTRA_SCORE, score);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            finishQuiz();
        } else {
            Toast.makeText(this, "Press back again to finish", Toast.LENGTH_SHORT).show();
        }

        backPressedTime = System.currentTimeMillis();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_SCORE, score);
        outState.putInt(KEY_QUESTION_COUNT, quizCounter);
        outState.putLong(KEY_MILLIS_LEFT, timeLeftInMillis);
        outState.putBoolean(KEY_ANSWERED, answered);
        outState.putParcelableArrayList(KEY_QUESTION_LIST, quizList);
    }
}
