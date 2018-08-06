package com.muilat.android.offlinetutorial;

import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.muilat.android.offlinetutorial.data.OfflineTutorialContract;
import com.muilat.android.offlinetutorial.data.Quiz;
import com.muilat.android.offlinetutorial.views.AnswerView;

import java.util.Random;

public class QuizActivity extends AppCompatActivity  implements
        AnswerView.OnAnswerSelectedListener{


    private TextView mQuestionText;
    private TextView mCorrectText;
    private AnswerView mAnswerSelect;

    Cursor quizCursor, quiz;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        mQuestionText = (TextView) findViewById(R.id.text_question);
        mCorrectText = (TextView) findViewById(R.id.text_correct);
        mAnswerSelect = (AnswerView) findViewById(R.id.answer_select);

        mAnswerSelect.setOnAnswerSelectedListener(this);
        
        //TODO: use loader
        quizCursor = getContentResolver().query(OfflineTutorialContract.QuizEntry.CONTENT_URI,
                null,
                null,
                null,
                null);

    quizCursor.moveToNext();

    //show question

        buildQuestion(quizCursor);


    }

    private void buildQuestion(Cursor quizCursor) {
        quizCursor.moveToPosition(new Random().nextInt(quizCursor.getCount()));

        Quiz quiz = new Quiz(quizCursor);
        mQuestionText.setText(quiz.getQuestion());

        mAnswerSelect.loadAnswers(quizCursor);
    }

    /* Answer Selection Callbacks */

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCorrectAnswerSelected() {
        updateResultText();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onWrongAnswerSelected() {
        updateResultText();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void updateResultText() {
        mCorrectText.setTextColor(mAnswerSelect.isCorrectAnswerSelected() ?
                getColor(R.color.colorCorrect) : getColor( R.color.colorWrong)
        );
        mCorrectText.setText(mAnswerSelect.isCorrectAnswerSelected() ?
                R.string.answer_correct : R.string.answer_wrong
        );
    }


}
