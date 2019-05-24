package com.muilat.android.offlinetutorial;

import android.app.Dialog;
import android.content.ContentValues;
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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.muilat.android.offlinetutorial.adapter.NotificationAdapter;
import com.muilat.android.offlinetutorial.data.Lessons;
import com.muilat.android.offlinetutorial.data.Notifications;
import com.muilat.android.offlinetutorial.data.OfflineTutorialContract;
import com.muilat.android.offlinetutorial.util.NetworkUtils;
import com.muilat.android.offlinetutorial.util.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class NotificationActivity extends AppCompatActivity  implements
        LoaderManager.LoaderCallbacks<Cursor>  {

    private static final int NOTIFICATION_LOADER_ID = 7745;
    private static final String TAG = NotificationActivity.class.getSimpleName();
    NotificationAdapter mNotificationAdapter;

    RecyclerView recycler;
    static LinearLayout emptyView, notification_layout;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

//        Toolbar toolbar = findViewById(R.id.toolbar);
//        TextView toolbar_title = findViewById(R.id.toolbar_title);
//        toolbar_title.setText(getString(R.string.favourites));
//        setSupportActionBar(toolbar);

        //adView
        LinearLayout adViewLinearLayout = findViewById(R.id.adViewLayout);
        Utils.loadAdView(this, adViewLinearLayout);


//
//        delete_all = findViewById(R.id.delete_all);
        emptyView = findViewById(R.id.emptyView);
        notification_layout = findViewById(R.id.notification_layout);

        mNotificationAdapter = new NotificationAdapter();


        recycler =  findViewById(R.id.notification_recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        recycler.setLayoutManager(layoutManager);

        recycler.setItemAnimator(new DefaultItemAnimator()); //Animator for recycler view
        recycler.setAdapter(mNotificationAdapter);
        recycler.setHasFixedSize(true);

        getSupportLoaderManager().initLoader(NOTIFICATION_LOADER_ID, null, this);









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

                try {
                    return getContentResolver().query(OfflineTutorialContract.NotificationEntry.CONTENT_URI,
                            null,
                            "status =?",
                            new String[]{"1"},
                            null);
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage()+" Failed to asynchronously load notifications.");
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

        mNotificationAdapter.swapCursor(data);
//        Log.e(TAG, "No of cursor: "+data.getCount());

        if(data.getCount()==0){
            notificationEmpty();
        }

    }


    @Override
    public void onLoaderReset(Loader<Cursor>loader) {
        mNotificationAdapter.swapCursor(null);
    }


    public static void notificationEmpty() {
        emptyView.setVisibility(View.VISIBLE);
        notification_layout.setVisibility(View.GONE);
    }

    public void onArrowBackClick(View view){
        finish();

    }



    @Override
    protected void onDestroy() {
        super.onDestroy();

        //clear notifications
        ContentValues contentValues = new ContentValues();
        contentValues.put(OfflineTutorialContract.NotificationEntry.COLUMN_STATUS, 0);
        getContentResolver().update(OfflineTutorialContract.NotificationEntry.CONTENT_URI,contentValues,"status=?",new String[]{"1"});
    }
}
