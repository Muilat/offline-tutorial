package com.muilat.android.offlinetutorial;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.muilat.android.offlinetutorial.data.LessonAdapter;
import com.muilat.android.offlinetutorial.data.Lessons;
import com.muilat.android.offlinetutorial.data.OfflineTutorialContract;

import java.util.ArrayList;

public class FavouriteActivity extends AppCompatActivity  implements
        LoaderManager.LoaderCallbacks<Cursor>  {

    private static final int FAVOURITE_LOADER_ID = 775;
    private static final String TAG = FavouriteActivity.class.getSimpleName();
    LessonAdapter mFavouriteAdapter;

    ArrayList<Lessons> mFavouriteArrayList = new ArrayList<>();

    public static FragmentTransaction fragmentTransaction;
    public static FragmentManager fragmentManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
//        Toolbar toolbar;
//        toolbar = findViewById(R.id.toolbar);
        setContentView(R.layout.activity_favourite);

        mFavouriteAdapter = new LessonAdapter();

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();


        RecyclerView recycler =  findViewById(R.id.favourite_recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        recycler.setLayoutManager(layoutManager);

        recycler.setItemAnimator(new DefaultItemAnimator()); //Animator for recycler view
        recycler.setAdapter(mFavouriteAdapter);
        recycler.setHasFixedSize(true);

        getSupportLoaderManager().initLoader(FAVOURITE_LOADER_ID, null, this);


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
                    return getContentResolver().query(OfflineTutorialContract.LessonEntry.FAVOURITE_CONTENT_URI,
                            null,
                            null,
                            null,
                            null);
//                    return Categories.dummyCategories();
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage()+" Failed to asynchronously load quiz.");
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
        for (int i = 0;  i< data.getCount(); i++) {
            data.moveToPosition(i);
            Lessons lesson = new Lessons(data);
            mFavouriteArrayList.add(lesson);

        }
        mFavouriteAdapter.swapCursor(mFavouriteArrayList);
//        Log.e(TAG, "No of cursor: "+data.getCount());
        Log.e(TAG, "No of Categories: "+data.getCount());


    }

    @Override
    public void onLoaderReset(Loader<Cursor>loader) {
        mFavouriteAdapter.swapCursor(null);
    }


}
