package com.muilat.android.offlinetutorial;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.NavUtils;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.muilat.android.offlinetutorial.data.Quiz;
import com.muilat.android.offlinetutorial.pref.OfflineTutorialPreference;

import java.util.ArrayList;
import java.util.HashMap;

public class ResultActivity extends AppCompatActivity {

    private static final String TAG = ResultActivity.class.getSimpleName();
    ArrayList<String> myAnsList=new ArrayList<String>();
    ArrayList<Quiz> questionsList=new ArrayList<>();

    RecyclerView recycler;

    private Quiz currentQuestion;

    ArrayList<HashMap<String, Object>> originalValues = new ArrayList<HashMap<String, Object>>();;

    HashMap<String, Object> temp = new HashMap<String, Object>();

    public static String KEY_QUES = "questions";
    public static String KEY_CANS = "canswer";
    public static String KEY_YANS = "yanswer";
    private InterstitialAd interstitialAd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_look);

//        AdView mAdView = (AdView) findViewById(R.id.adView);
//        AdRequest adRequest = new AdRequest.Builder()
//                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
//                .build();
//        mAdView.loadAd(adRequest);

        //get rating bar object
        RatingBar bar=findViewById(R.id.ratingBar1);
        bar.setNumStars(5);
        bar.setStepSize(0.5f);

        //get text view
        TextView tvCorrect =(TextView)findViewById(R.id.correct);
        TextView tvWrong =(TextView)findViewById(R.id.wrong);
        TextView t=(TextView)findViewById(R.id.textResult);
        recycler=findViewById(R.id.answer_recycler);


        //get score
        Bundle b = getIntent().getExtras();
        final int score= b.getInt("score");
        int totalQs= b.getInt("totalQs");
        myAnsList=b.getStringArrayList("myAnsList");
        questionsList=b.getParcelableArrayList("initial_quiz");

        //display score
        bar.setRating(score);

        tvCorrect.setText(score+"");
        tvWrong.setText(totalQs-score+"");

        float percentage=(score*100)/totalQs;

        if (percentage>=80 && percentage<=100){
            t.setText("Score is Excellent !");
        }else if(percentage>=70 && percentage<=79){
            t.setText("Score is Best");
        }else if(percentage>=60 && percentage<=69){
            t.setText("Score is Good");
        }else if(percentage>=50 && percentage<=59){
            t.setText("Score is Average!");
        }else if(percentage>=33 && percentage<=49){
            t.setText("Score is  Below Average!");
        }else{
            t.setText("Score is Poor! You need to practice more!");
        }


        Button btnShare=(Button)findViewById(R.id.btnShare);
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textToShare = "I scored "+score+" in Offline Tutorial Quiz, Checkout what youwill score";
                textToShare += "\nInstall from http://play.google.com/store/apps/details?id=" + getPackageName();

                Intent shareIntent = ShareCompat.IntentBuilder.from(ResultActivity.this)
                        .setText(textToShare)
                        .setChooserTitle("Share Developer with")
                        .setSubject(getString(R.string.app_name))
                        .setType("text/plain")
                        .createChooserIntent();

                if(shareIntent.resolveActivity(getPackageManager()) != null){
                    startActivity(shareIntent);
                }
            }
        });

        Button btnExit=(Button)findViewById(R.id.btnExit);
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInterstitialAd();
                finish();
            }
        });

        Button btnplayAgain=(Button)findViewById(R.id.btnPlayAgain);
        btnplayAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInterstitialAd();
                Intent quizIntent=new Intent(ResultActivity.this,QuizActivity.class);

                startActivity(quizIntent);

            }
        });


        for (int i = 0; i < questionsList.size(); i++) {
            currentQuestion=questionsList.get(i);
            temp = new HashMap<String, Object>();
            temp.put(KEY_QUES,  currentQuestion.getQuestion());
            temp.put(KEY_CANS, currentQuestion.getAnswer());
            temp.put(KEY_YANS, myAnsList.get(i));

            // add the row to the ArrayList
            originalValues.add(temp);

        }

        recycler =  findViewById(R.id.answer_recycler);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recycler.setLayoutManager(layoutManager);

        recycler.setItemAnimator(new DefaultItemAnimator()); //Animator for recycler view
        recycler.setAdapter(new AnswerAdapter());
        recycler.setHasFixedSize(true);

        //interstitialAd
        interstitialAd = new InterstitialAd(ResultActivity.this);
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                Log.e(TAG, "ad closed");
                interstitialAd.loadAd(new AdRequest.Builder().build());
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                super.onAdFailedToLoad(errorCode);
                Log.e(TAG, "ad failed");

            }

            @Override
            public void onAdLeftApplication() {
                super.onAdLeftApplication();
                Log.e(TAG, "ad left application");

            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
                Log.e(TAG, "ad opened");

            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                Log.e(TAG, "ad loaded");

            }
        });

        //interstitialAd
        Handler handlerInterstitialAd = new Handler();
        handlerInterstitialAd.postDelayed(new Runnable() {
            @Override
            public void run() {
                interstitialAd.setAdUnitId(OfflineTutorialPreference.getInterstitialId(ResultActivity.this));

                interstitialAd.loadAd(new AdRequest.Builder().build());
            }
        },3000);


    }

    private class AnswerAdapter extends  RecyclerView.Adapter<AnswerAdapter.ViewHolder> {
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            final ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(ResultActivity.this)
                    .inflate(R.layout.lists_row, parent, false));
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.tvQS.setText(originalValues.get(position).get(KEY_QUES)
                    .toString());

            String correctAnswer, yourAnswer;
            correctAnswer = originalValues.get(position)
                    .get(KEY_CANS).toString();
            yourAnswer = originalValues.get(position)
                    .get(KEY_YANS).toString();
            if (correctAnswer.equals(yourAnswer)) {
                holder.tvYouans.setVisibility(View.GONE);
                holder.answerCard.setBackgroundColor(getResources().getColor(R.color.colorGreenLight));
            } else {
                holder.answerCard.setBackgroundColor(getResources().getColor(R.color.colorRedLight));
                holder.tvYouans.setText("Your Ans: " + yourAnswer);
            }
            holder.tvCans.setText("Correct Ans: " + correctAnswer);

        }

        @Override
        public int getItemCount() {
            return originalValues.size();
        }

        // define your custom adapter

        // class for caching the views in a row
        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView tvQS, tvCans, tvYouans;
            CardView answerCard;

            public ViewHolder(View itemView) {
                super(itemView);
                tvQS = (TextView) itemView.findViewById(R.id.tvQuestions);
                tvCans = (TextView) itemView.findViewById(R.id.tvCorrectAns);
                tvYouans = (TextView) itemView.findViewById(R.id.tvYourAns);
                answerCard = itemView.findViewById(R.id.answer_card);

            }
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showInterstitialAd(){
        if(interstitialAd != null && interstitialAd.isLoaded()){
            interstitialAd.show();
        }
    }
}
