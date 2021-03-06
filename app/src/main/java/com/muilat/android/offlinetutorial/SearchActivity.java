package com.muilat.android.offlinetutorial;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.muilat.android.offlinetutorial.data.LessonAdapter;
import com.muilat.android.offlinetutorial.data.Lessons;
import com.muilat.android.offlinetutorial.data.OfflineTutorialContract.LessonEntry;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>  {

    private static final int SEARCH_LOADER_ID = 1324;
    private static final String TAG = SearchActivity.class.getSimpleName();
    private LessonAdapter mLessonAdapter;

    ArrayList<Lessons> mLessons = new ArrayList<>();

    Cursor mLessonsCursor;

    String queryText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);





        SearchView searchView = findViewById(R.id.searchView);
        searchView.setIconified(false);
        searchView.setIconifiedByDefault(true);
        searchView.requestFocusFromTouch();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                queryText = newText ;
                if(newText.equals(""))
                    return false;

                //Ensure a loader is initialized and active
                getSupportLoaderManager().initLoader(SEARCH_LOADER_ID, null, SearchActivity.this);

                return true;
            }
        });

//        searchView.setSuggestionsAdapter();

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

                String wildCardQuery = "%"+queryText+"%";

                try {
                    return getContentResolver().query(LessonEntry.CONTENT_URI,
                            null,
                            LessonEntry.COLUMN_TITLE+" LIKE ? OR "+LessonEntry.COLUMN_DESCRIPTION+" LIKE ? ",
                            new String[]{wildCardQuery,wildCardQuery},
                            null);
//                    return Categories.dummyCategories();
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage()+" Failed to asynchronously load Search results.");
                    e.printStackTrace();
                    return null;
                }
            }

            public void deliverResult(Cursor data) {
                mLessonsCursor = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.e(TAG, "No of Search results: "+data.getCount());

//        while(data.moveToNext()){
//            mLessons.add(new Lessons(data));
//        }
        for (int i = 0;  i< data.getCount(); i++) {
            mLessonsCursor.moveToPosition(i);
            Lessons lesson = new Lessons(mLessonsCursor);
            mLessons.add(lesson);

        }
        mLessonAdapter.swapCursor(mLessons);

//        setLessonView(data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor>loader) {
        mLessons = null;

//        mLessonAdapter.swapCursor(null);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        // When the home button is pressed, take the user back to the previous Activity
        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
    }

    public void closeSearch(View view) {
        finish();
    }
}
