package com.muilat.android.offlinetutorial;

import android.content.ClipData;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.muilat.android.offlinetutorial.adapter.LessonViewAdapter;
import com.muilat.android.offlinetutorial.data.Lessons;
import com.muilat.android.offlinetutorial.data.OfflineTutorialContract;
import com.muilat.android.offlinetutorial.pref.OfflineTutorialPreference;

import java.util.ArrayList;

public class LessonViewFragment extends AppCompatActivity {

    private static final String TAG = LessonViewFragment.class.getName();
    public static final String ARG_LESSON_POSITION = "lesson_positon";
    public static final String ARG_LESSONS = "lessons";

    private static LessonViewAdapter mLessonViewAdapter;

    ImageView favouriteIcon, copyIcon, searchIcon, shareIcon;
    TextView total,number;
    
    WebView webView;
    WebSettings settings;


    public ArrayList<Lessons> mLesson = new ArrayList<>();
    Lessons lesson;
    static int mPosition = 0;
    private InterstitialAd interstitialAd;
    private String text_size_pref;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        copyIcon = findViewById(R.id.content_copy);
        searchIcon = findViewById(R.id.action_search);
        shareIcon = findViewById(R.id.share);
        favouriteIcon = findViewById(R.id.favourite);
        total = findViewById(R.id.lesson_total);
        number = findViewById(R.id.lesson_number);
        TextView divider = findViewById(R.id.divider);
        RelativeLayout container = findViewById(R.id.container);

        //TODO: to be removed on request
        copyIcon.setVisibility(View.GONE);
        shareIcon.setVisibility(View.GONE);
        total.setVisibility(View.GONE);
        number.setVisibility(View.GONE);
        divider.setVisibility(View.GONE);


        TextView toolbar_title = findViewById(R.id.toolbar_title);

        webView = findViewById(R.id.webView);
        settings = webView.getSettings();
        settings.setDefaultTextEncodingName("utf-8");

        int width = container.getMeasuredWidth();



        if(savedInstanceState != null){
            mLesson = savedInstanceState.getParcelableArrayList(ARG_LESSONS);
            mPosition = savedInstanceState.getInt(ARG_LESSON_POSITION);
        }
        else if(getIntent() != null) {
            mLesson = getIntent().getParcelableArrayListExtra(ARG_LESSONS);
            mPosition = getIntent().getIntExtra(ARG_LESSON_POSITION, mPosition);
            lesson = mLesson.get(mPosition);


        }
        
        lesson = mLesson.get(mPosition);

        Double d = new Double(width * 0.9);
        Log.e(TAG, "width: "+width);
        int i = d.intValue();
        Log.e(TAG, "i: "+i);

        String title = lesson.getTitle();


        if(title.length() >= i){
            i = i - 3;
            try {
                title = title.substring(0,i)+"...";

            }catch(Exception e){
                toolbar_title.setText(title);
                Log.e(TAG, "exception: "+e.getMessage());

            }

        }
        toolbar_title.setText(title);


        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        text_size_pref = sharedPreferences.getString(getString(R.string.pref_text_size_key),
                getResources().getString(R.string.pref_text_size_small_value));

        if (text_size_pref.equals(getResources().getString(R.string.pref_text_size_large_value))) {
            settings.setDefaultFontSize((int) getResources().getDimension(R.dimen.text_large));

        } else if (text_size_pref.equals(getResources().getString(R.string.pref_text_size_medium_value))) {
            settings.setDefaultFontSize((int) getResources().getDimension(R.dimen.text_medium));

        } else {
            settings.setDefaultFontSize((int) getResources().getDimension(R.dimen.text_small));

        }
//        webView.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
//        webView.getSettings().setUseWideViewPort(true);
        webView.loadData(lesson.getDescription(),"text/html; charset=utf-8",null);



        checkFavourite(mPosition);
        number.setText(mPosition+1+"");
        total.setText(mLesson.size()+"");



        favouriteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: go get the lesson
                Lessons lesson = mLesson.get(mPosition);

                String stringId = Long.toString(lesson.getID());
                Uri uri = OfflineTutorialContract.LessonEntry.CONTENT_URI;
                uri = uri.buildUpon().appendPath(stringId).build();

                ContentValues contentValues = new ContentValues();
                if(lesson.isFavourite()){
                    contentValues.put(OfflineTutorialContract.LessonEntry.COLUMN_IS_FAVOURITE, 0);
                    Toast.makeText(LessonViewFragment.this, lesson.getTitle()+" is removed from favourite", Toast.LENGTH_SHORT).show();
                    favouriteIcon.setImageResource(R.drawable.ic_favorite_border_white_24dp);
                    lesson.setIsFavourite("0");

                }
                else {
                    contentValues.put(OfflineTutorialContract.LessonEntry.COLUMN_IS_FAVOURITE, 1);
                    Toast.makeText(LessonViewFragment.this, lesson.getTitle()+" is added to favourite", Toast.LENGTH_SHORT).show();
                    favouriteIcon.setImageResource(R.drawable.ic_favorite_white_24dp);
                    lesson.setIsFavourite("1");

                }
                getContentResolver().update(uri,contentValues,null,null);


            }
        });

        copyIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Lessons lesson = mLesson.get(mPosition);

                String textToCopy = lesson.getTitle()+"\n"+lesson.getDescription();
                if(Build.VERSION.SDK_INT<Build.VERSION_CODES.HONEYCOMB){
                    android.text.ClipboardManager clipboardManager = (android.text.ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
                    clipboardManager.setText(textToCopy);
                }
                else {
                    android.content.ClipboardManager clipboardManager = (android.content.ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
                    android.content.ClipData clipData = ClipData.newPlainText("Copied text" ,textToCopy);
                    clipboardManager.setPrimaryClip(clipData);
                }
                Toast.makeText(LessonViewFragment.this, "Copied to clipboard", Toast.LENGTH_SHORT).show();

            }
        });

        shareIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Lessons lesson = mLesson.get(mPosition);
                String textToShare = lesson.getTitle()+"\n"+lesson.getDescription();
                textToShare += "\nInstall from http://play.google.com/store/apps/details?id=" + getPackageName();

                Intent shareIntent = ShareCompat.IntentBuilder.from(LessonViewFragment.this)
                        .setText(textToShare)
                        .setChooserTitle("Share Lesson with")
                        .setSubject(getString(R.string.app_name))
                        .setType("text/plain")
                        .createChooserIntent();

                if(shareIntent.resolveActivity(getPackageManager()) != null){
                    startActivity(shareIntent);
                }
            }
        });

        //interstitialAd
        interstitialAd = new InterstitialAd(LessonViewFragment.this);
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
                interstitialAd.setAdUnitId(OfflineTutorialPreference.getInterstitialId(LessonViewFragment.this));

                interstitialAd.loadAd(new AdRequest.Builder().build());
            }
        },1000);

    }

    private void checkFavourite(int position) {
        Lessons lesson = mLesson.get(position);
        Log.e(TAG, lesson.mIsFavourite);
        if (lesson.isFavourite()){
            favouriteIcon.setImageResource(R.drawable.ic_favorite_white_24dp);
        }
        else
            favouriteIcon.setImageResource(R.drawable.ic_favorite_border_white_24dp);
    }


//    public static Lessons getLessonInViewId(){
//        Lessons lesson = mLesson.get(position);
//
//        return lesson;
//    }

    public void onArrowBackClick(View view){

        finish();
    }

    @Override
    public void onBackPressed() {
        showInterstitialAd();
        finish();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(ARG_LESSON_POSITION, mPosition);
        outState.putParcelableArrayList(ARG_LESSONS, mLesson);
        super.onSaveInstanceState(outState);
    }

    private void showInterstitialAd(){
        if(interstitialAd != null && interstitialAd.isLoaded()){
            interstitialAd.show();
        }
    }
}
