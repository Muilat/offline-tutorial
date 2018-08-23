package com.muilat.android.offlinetutorial;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.muilat.android.offlinetutorial.adapter.LessonAdapter;
import com.muilat.android.offlinetutorial.data.Lessons;
import com.muilat.android.offlinetutorial.data.OfflineTutorialContract;
import com.muilat.android.offlinetutorial.data.SubCategories;
import com.muilat.android.offlinetutorial.pref.OfflineTutorialPreference;
import com.muilat.android.offlinetutorial.util.NetworkUtils;
import com.muilat.android.offlinetutorial.util.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class LessonActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    public static final String EXTRA_SUB_CATEGORY = "extra_sub_category";

    private static final int LESSON_LOADER_ID = 22722;
    private static final String TAG = LessonActivity.class.getSimpleName();


    SubCategories sub_category;
    ArrayList<Lessons> mLessons = new ArrayList<>();
    LessonAdapter mLessonAdapter;

    LinearLayout adviewLinearLayout;
    public String bannerId;
    private InterstitialAd interstitialAd;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar toolbar;
        toolbar = findViewById(R.id.toolbar);
        setContentView(R.layout.activity_lessons);

        TextView toolbar_title = findViewById(R.id.toolbar_title);





        if (getIntent() != null) {
            sub_category = getIntent().getParcelableExtra(EXTRA_SUB_CATEGORY);
        }

        toolbar_title.setText(sub_category.getTitle());
        setSupportActionBar(toolbar);

        mLessonAdapter = new LessonAdapter();

        RecyclerView recycler =  findViewById(R.id.lessons_recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        recycler.setLayoutManager(layoutManager);

        recycler.setItemAnimator(new DefaultItemAnimator()); //Animator for recycler view
        recycler.setAdapter(mLessonAdapter);
        recycler.setHasFixedSize(true);

        getSupportLoaderManager().initLoader(LESSON_LOADER_ID, null, this);


        //adView
        LinearLayout adViewLinearLayout = findViewById(R.id.adViewLayout);
        Utils.loadAdView(this, adViewLinearLayout);

        //interstitialAd
        interstitialAd = new InterstitialAd(LessonActivity.this);
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
                interstitialAd.setAdUnitId(OfflineTutorialPreference.getInterstitialId(LessonActivity.this));

                interstitialAd.loadAd(new AdRequest.Builder().build());
            }
        },3000);





    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<Cursor>(this) {

            // Initialize a Cursor, this will hold all the Category data
//            Cursor mCategoriesData = null;
            Cursor mLessonsData;

            // onStartLoading() is called when a loader first starts loading data
            @Override
            protected void onStartLoading() {
                if (mLessonsData != null) {
                    // Delivers any previously loaded data immediately
                    deliverResult(mLessonsData);
                } else {
                    // Force a new load
                    forceLoad();
                }
            }

            // loadInBackground() performs asynchronous loading of data
            @Override
            public Cursor loadInBackground() {

                String stringId = Long.toString(sub_category.getId());
                Uri uri = OfflineTutorialContract.LessonEntry.CONTENT_URI;
                uri = uri.buildUpon().appendPath(stringId).build();


                try {
                    return getContentResolver().query(uri,
                            null,
                            null,
                            null,
                            null);
//                    return Categories.dummyCategories();
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage()+" Failed to asynchronously load Lessons.");
                    e.printStackTrace();
                    return null;
                }
            }

            public void deliverResult(Cursor data) {
                mLessonsData = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.e(TAG, "No of Lessons: "+data.getCount());

        for (int i = 0;  i< data.getCount(); i++) {
            data.moveToPosition(i);
            Lessons lesson = new Lessons(data);
            mLessons.add(lesson);

        }
        mLessonAdapter.swapCursor(mLessons);

//        setLessonView(data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor>loader) {
//        mLessons = null;

        mLessonAdapter.swapCursor(null);
    }




    public void onArrowBackClick(View view){
        finish();
    }

    @Override
    public void onBackPressed() {
         finish();
    }

    public void onSearchClick(View view){
        showInterstitialAd();
        Intent searchIntent = new Intent(this, SearchActivity.class);
        searchIntent.putParcelableArrayListExtra(SearchActivity.EXTRA_SEARCHES,mLessons);;
        startActivity(searchIntent);

    }

    private void showInterstitialAd(){
        if(interstitialAd != null && interstitialAd.isLoaded()){
            interstitialAd.show();
        }
    }


}
