package com.muilat.android.offlinetutorial;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;

import com.muilat.android.offlinetutorial.adapter.LessonAdapter;
import com.muilat.android.offlinetutorial.data.Lessons;
import com.muilat.android.offlinetutorial.data.OfflineTutorialDbHelper;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int SEARCH_LOADER_ID = 1324;
    private static final String TAG = SearchActivity.class.getSimpleName();
    public static final String EXTRA_SEARCHES = "extra-searches";
    public static final String EXTRA_SUB_CATEGORY_ID = "extra-sub-category-id";
    int mSubCategoryId = 0;
    private LessonAdapter mSearchAdapter;

    private SQLiteDatabase mDb;
    private OfflineTutorialDbHelper dbHelper;


    String queryText = "SELECT * FROM lessons WHERE _id = -1";//yes wrong query dont want anything returned

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getIntent().hasExtra(EXTRA_SUB_CATEGORY_ID)){
            mSubCategoryId = getIntent().getIntExtra(EXTRA_SUB_CATEGORY_ID,0);
        }



        RecyclerView recycler =  findViewById(R.id.search_recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        recycler.setLayoutManager(layoutManager);

        recycler.setItemAnimator(new DefaultItemAnimator()); //Animator for recycler view


        SearchView searchView = findViewById(R.id.searchView);
        searchView.setIconified(false);
        searchView.setIconifiedByDefault(true);
        searchView.requestFocusFromTouch();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);



        mSearchAdapter = new LessonAdapter();
        recycler.setAdapter(mSearchAdapter);
        recycler.setHasFixedSize(true);

        dbHelper = new OfflineTutorialDbHelper(this);
        mDb= dbHelper.getReadableDatabase();

        getSupportLoaderManager().initLoader(SEARCH_LOADER_ID, null, SearchActivity.this);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
//                mSearchAdapter.getFilter().filter(newText);

                if(newText.equals("")){

                    mSearchAdapter.swapCursor(null);
                }else{
                    if(mSubCategoryId != 0){
                        queryText = "SELECT * FROM lessons WHERE sub_category_id = "+mSubCategoryId+" AND ((title LIKE '%"+newText+"%'"
                                +") OR (description LIKE '%"+newText+"%'))";
                    }
                    else {
                        queryText = "SELECT * FROM lessons WHERE (title LIKE '%" + newText + "%'"
                                + " OR description LIKE '%" + newText + "%')";
                    }
                    getSupportLoaderManager().restartLoader(SEARCH_LOADER_ID, null, SearchActivity.this);

                }

                return false;
            }
        });




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
                    Cursor cursor = mDb.rawQuery(queryText, null);

                    return cursor;
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
//        Log.e(TAG, "No of Lessons: "+data.getCount());

        ArrayList<Lessons> lessons = new ArrayList<>();

        for (int i = 0;  i< data.getCount(); i++) {
            data.moveToPosition(i);
            Lessons lesson = new Lessons(data);
            lessons.add(lesson);

        }
        mSearchAdapter.swapCursor(lessons);

//        setLessonView(data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor>loader) {
        mSearchAdapter.swapCursor(null);
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
