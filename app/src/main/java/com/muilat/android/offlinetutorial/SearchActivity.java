package com.muilat.android.offlinetutorial;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;

import com.muilat.android.offlinetutorial.adapter.SearchAdapter;
import com.muilat.android.offlinetutorial.data.Lessons;
import com.muilat.android.offlinetutorial.data.OfflineTutorialContract.LessonEntry;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

//    private static final int SEARCH_LOADER_ID = 1324;
    private static final String TAG = SearchActivity.class.getSimpleName();
    public static final String EXTRA_SEARCHES = "extra-searches";
    public static final String EXTRA_POSITION = "extra-position";
    int mPosition = -1;
    private SearchAdapter mSearchAdapter;

    ArrayList<Lessons> mLessons = new ArrayList<>();

    Cursor mLessonsCursor;

    String queryText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        if (getIntent().hasExtra(EXTRA_SEARCHES)){
//            mLessons = getIntent().getParcelableArrayListExtra(EXTRA_SEARCHES);
//        }else{
            mLessonsCursor = getContentResolver().query(LessonEntry.CONTENT_URI,null,null,null,null);

            while (mLessonsCursor.moveToNext()){
                mLessons.add(new Lessons(mLessonsCursor));
            }
//        }



        RecyclerView recycler =  findViewById(R.id.search_recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        recycler.setLayoutManager(layoutManager);

        recycler.setItemAnimator(new DefaultItemAnimator()); //Animator for recycler view


        SearchView searchView = findViewById(R.id.searchView);
        searchView.setIconified(false);
        searchView.setIconifiedByDefault(true);
        searchView.requestFocusFromTouch();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);



        mSearchAdapter = new SearchAdapter(this, mLessons);
        recycler.setAdapter(mSearchAdapter);
        recycler.setHasFixedSize(true);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mSearchAdapter.getFilter().filter(newText);

                return false;
            }
        });


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
