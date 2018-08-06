package com.muilat.android.offlinetutorial;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.muilat.android.offlinetutorial.data.LessonAdapter;
import com.muilat.android.offlinetutorial.data.Lessons;
import com.muilat.android.offlinetutorial.data.OfflineTutorialContract;

import java.util.ArrayList;

public class LessonListFragment  extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor>  {
    int sub_category_id = -1;
    private static final int LESSON_LOADER_ID = 2222;
    private static final String TAG = LessonListFragment.class.getName();

    public static final String ARG_SUB_CATEGORY_ID = "sub_category_id";

    private LessonAdapter mLessonAdapter;

    ArrayList<Lessons> mLessons = new ArrayList<>();

    Cursor mLessonsCursor;



    public LessonListFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            sub_category_id = getArguments().getInt(ARG_SUB_CATEGORY_ID);
        }


        //Ensure a loader is initialized and active
        getActivity().getSupportLoaderManager().initLoader(LESSON_LOADER_ID, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_lessons, container, false);

        mLessonAdapter = new LessonAdapter();

        RecyclerView recycler =  view.findViewById(R.id.lessons_recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,false);
        recycler.setLayoutManager(layoutManager);

        recycler.setItemAnimator(new DefaultItemAnimator()); //Animator for recycler view
        recycler.setAdapter(mLessonAdapter);
        recycler.setHasFixedSize(true);
//TODO: showing loading bar

        return view;

    }


        @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<Cursor>(getActivity()) {

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

                String stringId = Long.toString(sub_category_id);
                Uri uri = OfflineTutorialContract.LessonEntry.CONTENT_URI;
                uri = uri.buildUpon().appendPath(stringId).build();


                try {
                    return getActivity().getContentResolver().query(uri,
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
                mLessonsCursor = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.e(TAG, "No of Lessons: "+data.getCount());

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

    private void setLessonArrayList(Cursor data){
        while(data.moveToNext()){
//            mLessons.add(new Lessons(data));
        }


    }
}
