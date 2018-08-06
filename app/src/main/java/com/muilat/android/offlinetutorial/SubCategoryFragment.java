package com.muilat.android.offlinetutorial;

import android.database.Cursor;
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
import android.widget.Toast;

import com.muilat.android.offlinetutorial.data.SubCategories;
import com.muilat.android.offlinetutorial.data.SubCategoryAdapter;

import java.util.ArrayList;

import static com.muilat.android.offlinetutorial.data.OfflineTutorialContract.SubCategoryEntry.CONTENT_URI;


public class SubCategoryFragment extends Fragment implements  LoaderManager.LoaderCallbacks<Cursor>  {

    private static final String TAG = SubCategoryFragment.class.getSimpleName();
    private static final int SUB_CATEGORY_LOADER_ID = 11123;
    int category_id = -1;

    public final static String ARG_CATEGORY_ID = "category_id";

    static SubCategoryAdapter mSubCategoryAdapter;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Bundle bundle = getArguments();
            category_id = bundle.getInt(ARG_CATEGORY_ID);
        }

        //Ensure a loader is initialized and active
        getActivity().getSupportLoaderManager().initLoader(SUB_CATEGORY_LOADER_ID, null, this);
    }

    public SubCategoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_sub_category, container, false);

        RecyclerView recycler =  view.findViewById(R.id.sub_cat_recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        recycler.setLayoutManager(layoutManager);

        mSubCategoryAdapter = new SubCategoryAdapter();
        recycler.setItemAnimator(new DefaultItemAnimator()); //Animator for recycler view
        recycler.setAdapter(mSubCategoryAdapter);
        recycler.setHasFixedSize(true);

        return view;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<Cursor>(getActivity()) {

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
                String selection = null;
                String[] selectionArgs = null;
                if(category_id > 0){
                    selection = "category_id=?";
                    selectionArgs = new String[]{String.valueOf((category_id))};
                }

                try {
                    return getActivity().getContentResolver().query(CONTENT_URI,
                            null,
                            selection,
                            selectionArgs,
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
//        getActivity().getSupportLoaderManager().restartLoader(SUB_CATEGORY_LOADER_ID, null, this);
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
