package com.muilat.android.offlinetutorial;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.muilat.android.offlinetutorial.adapter.SubCategoryAdapter;
import com.muilat.android.offlinetutorial.data.OfflineTutorialContract;
import com.muilat.android.offlinetutorial.data.SubCategories;
import com.muilat.android.offlinetutorial.pref.OfflineTutorialPreference;
import com.muilat.android.offlinetutorial.util.Utils;


public class SubCategoryFragment extends AppCompatActivity implements  LoaderManager.LoaderCallbacks<Cursor>  {

    private static final String TAG = SubCategoryFragment.class.getSimpleName();
    private static final int SUB_CATEGORY_LOADER_ID = 11123;
    public static final String ARG_CATEGORY_TITLE = "category_title";
    int category_id = -1;

    public final static String ARG_CATEGORY_ID = "category_id";

    static SubCategoryAdapter mSubCategoryAdapter;
    private InterstitialAd interstitialAd;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sub_category);
        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView title = findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);






        RecyclerView recycler =  findViewById(R.id.sub_cat_recyclerView);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this,2);
        recycler.setLayoutManager(layoutManager);

        mSubCategoryAdapter = new SubCategoryAdapter();
        recycler.setItemAnimator(new DefaultItemAnimator()); //Animator for recycler view
        recycler.setAdapter(mSubCategoryAdapter);
        recycler.setHasFixedSize(true);

        if (getIntent() != null) {
            category_id = getIntent().getIntExtra(ARG_CATEGORY_ID, 0);
            title.setText(getIntent().getStringExtra(ARG_CATEGORY_TITLE));
        }

        //Ensure a loader is initialized and active
        getSupportLoaderManager().initLoader(SUB_CATEGORY_LOADER_ID, null, this);

        //adView
        LinearLayout adViewLinearLayout = findViewById(R.id.adViewLayout);
        Utils.loadAdView(this, adViewLinearLayout);

        //interstitialAd
        interstitialAd = new InterstitialAd(SubCategoryFragment.this);
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
                interstitialAd.setAdUnitId(OfflineTutorialPreference.getInterstitialId(SubCategoryFragment.this));

                interstitialAd.loadAd(new AdRequest.Builder().build());
            }
        },1000);

    }

    public void onSubCategoryClicked(View view){

        SubCategories subCategory = (SubCategories) view.findViewById(R.id.sub_cat_card).getTag();


        //go to details
        Intent detailsIntent = new Intent(this, LessonActivity.class);
        detailsIntent.putExtra(LessonActivity.EXTRA_SUB_CATEGORY, subCategory);
        startActivity(detailsIntent);


    }




    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<Cursor>(SubCategoryFragment.this) {

            // Initialize a Cursor, this will hold all the Category data
            Cursor mSubCategorysData = null;

            // onStartLoading() is called when a loader first starts loading data
            @Override
            protected void onStartLoading() {
                if (mSubCategorysData != null) {
                    // Delivers any previously loaded data immediately
                    deliverResult(mSubCategorysData);
                } else {
                    // Force a new load
                    forceLoad();
                }
            }

            // loadInBackground() performs asynchronous loading of data
            @Override
            public Cursor loadInBackground() {
                String stringId = Long.toString(category_id);
                Uri uri = OfflineTutorialContract.SubCategoryEntry.CONTENT_URI;
                uri = uri.buildUpon().appendPath(stringId).build();


                try {
                    return getContentResolver().query(uri,
                            null,
                            null,
                            null,
                            null);
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage()+" Failed to asynchronously load Categorys.");
                    e.printStackTrace();
                    return null;
                }
            }

            public void deliverResult(Cursor data) {
                mSubCategorysData = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mSubCategoryAdapter.swapCursor(data);
//        Log.e(TAG, "No of cursor: "+data.getCount());
        Log.e(TAG, "No of Subcategories: "+data.getCount());

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mSubCategoryAdapter.swapCursor(null);
    }


    @Override
    public void onResume() {
        super.onResume();

        // re-queries for all SubCategories
        //getSupportLoaderManager().restartLoader(SUB_CATEGORY_LOADER_ID, null, this);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public void onArrowBackClick(View view){
        showInterstitialAd();
        finish();
    }

    @Override
    public void onBackPressed() {
        showInterstitialAd();
        finish();
    }

    public void onSearchClick(View view){
        Intent searchIntent = new Intent(this, SearchActivity.class);
//        searchIntent.putParcelableArrayListExtra(SearchActivity.EXTRA_SEARCHES,mLessons);;
        startActivity(searchIntent);

    }

    private void showInterstitialAd(){
        if(interstitialAd != null && interstitialAd.isLoaded()){
            interstitialAd.show();
        }
    }


}
