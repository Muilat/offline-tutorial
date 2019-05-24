package com.muilat.android.offlinetutorial;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.muilat.android.offlinetutorial.data.Lessons;
import com.muilat.android.offlinetutorial.data.OfflineTutorialContract;
import com.muilat.android.offlinetutorial.data.Quiz;
import com.muilat.android.offlinetutorial.util.NetworkUtils;
import com.muilat.android.offlinetutorial.util.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Set;

public class QuizActivity extends AppCompatActivity  {

    public static final String EXTRA_QUIZ_SET_ID = "extra-quiz-set-id";
    private ArrayList<Quiz> questionsList = new ArrayList<>();
    private Quiz currentQuestion;

    private TextView txtQuestion,tvNoOfQs;
    private RadioButton rbtnA, rbtnB, rbtnC,rbtnD;
    private Button btnNext;

    private int obtainedScore=0;
    private int questionId=0;

    private int answeredQsNo = 0;

    ArrayList<String> myAnsList ;
    ArrayList<Quiz> initialQuiz = new ArrayList<>();

    Cursor mCursor;
    private int quiz_set_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        //adView
        LinearLayout adViewLinearLayout = findViewById(R.id.adViewLayout);
        Utils.loadAdView(this, adViewLinearLayout);

        if (getIntent() != null) {
            quiz_set_id = getIntent().getIntExtra(EXTRA_QUIZ_SET_ID, 0);
        }


        //Initialize the view
        init();

        String id = Long.toString(quiz_set_id);
        Uri uri = OfflineTutorialContract.QuizEntry.CONTENT_URI;
        uri = uri.buildUpon().appendPath(id).build();
        Cursor quizCursor= getContentResolver().query(uri,null,null,null,null);

        mCursor = quizCursor;
        int quiz_number;
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(QuizActivity.this);
        quiz_number = Integer.parseInt(sharedPreferences.getString(getString(R.string.pref_quiz_no_key),getString(R.string.pref_quiz_no_default)));

        if(quiz_number > quizCursor.getCount())
            quiz_number = quizCursor.getCount();
        Random random = new Random();
        Set<Integer> quizSet = new LinkedHashSet<>();

        while(quizSet.size() < quiz_number){
            int i = random.nextInt(quizCursor.getCount());
            if(quizSet.contains(i))
                continue;
            else
                quizSet.add(i);

        }

//        questionsList = (ArrayList)quizSet;

        for (int i:quizSet) {
            quizCursor.moveToPosition(i);
            questionsList.add(new Quiz(quizCursor));
        }


//        while(quizCursor.moveToNext()) {
//            questionsList.add(new Quiz(quizCursor));
//        }
        currentQuestion=questionsList.get(questionId);

        //Set question
        setQuestionsView();

        //Check and Next
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioGroup grp = findViewById(R.id.radioGroup1);
                RadioButton answer= findViewById(grp.getCheckedRadioButtonId());

                Log.e("Answer ID", "Selected Positioned  value - "+grp.getCheckedRadioButtonId());

                if(answer!=null){
                    Log.e("Answer", currentQuestion.getAnswer() + " -- " + answer.getText());
                    //Add answer to the list
                    myAnsList.add(""+answer.getText());
                    initialQuiz.add(currentQuestion);

                    if(currentQuestion.getAnswer().equals(answer.getText())){
                        obtainedScore++;
                        Log.e("comments", "Correct Answer");
                        Log.d("score", "Obtained score " + obtainedScore);
                    }else{
                        Log.e("comments", "Wrong Answer");
                    }
                    if(questionId<mCursor.getCount()){
                        currentQuestion=questionsList.get(questionId);
                        setQuestionsView();
                    }else{
                        Intent intent = new Intent(QuizActivity.this, ResultActivity.class);

                        Bundle b = new Bundle();
                        b.putInt("score", obtainedScore);
                        b.putInt("totalQs", questionsList.size());
                        b.putStringArrayList("myAnsList", myAnsList);
                        b.putParcelableArrayList("initial_quiz", initialQuiz);
                        intent.putExtras(b);
                        startActivity(intent);
                        finish();

                    }

                }else{

                    Toast.makeText(QuizActivity.this,"Please, select an answer", Toast.LENGTH_SHORT).show();
                    Log.e("comments", "No Answer");
                }

                //Need to clear the checked item id
                grp.clearCheck();


            }//end onClick Method
        });


    }

    public void init(){
        tvNoOfQs=findViewById(R.id.tvNumberOfQuestions);
        txtQuestion=findViewById(R.id.tvQuestion);
        rbtnA=findViewById(R.id.radio0);
        rbtnB=findViewById(R.id.radio1);
        rbtnC=findViewById(R.id.radio2);
        rbtnD=findViewById(R.id.radio3);

        btnNext=findViewById(R.id.btnNext);

        myAnsList = new ArrayList<>();
    }


    private void setQuestionsView()
    {
        rbtnA.setChecked(false);
        rbtnB.setChecked(false);
        rbtnC.setChecked(false);
        rbtnD.setChecked(false);

        answeredQsNo=questionId+1;
        tvNoOfQs.setText("Questions "+answeredQsNo+" of "+questionsList.size());

        txtQuestion.setText(currentQuestion.getQuestion());
        rbtnA.setText(currentQuestion.getOption1());
        rbtnB.setText(currentQuestion.getOption2());
        rbtnC.setText(currentQuestion.getOption3());
        rbtnD.setText(currentQuestion.getOption4());

        questionId++;
    }


    @Override
    public void onBackPressed() {
        final Dialog exitQuizDialog = new Dialog(QuizActivity.this);

        exitQuizDialog.setContentView(R.layout.negative_dialog);
        ImageView closeDialog = exitQuizDialog.findViewById(R.id.close_dialog);
        Button confrimExit = exitQuizDialog.findViewById(R.id.confirm);
        TextView dialogMessage = exitQuizDialog.findViewById(R.id.dialog_message);

        confrimExit.setText(R.string.exit);
        dialogMessage.setText(R.string.exit_message);


        closeDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exitQuizDialog.dismiss();
            }
        });

        confrimExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(QuizActivity.this, "Quiz exited!", Toast.LENGTH_SHORT).show();
                exitQuizDialog.dismiss();
                finish();
            }
        });

        exitQuizDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        exitQuizDialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
