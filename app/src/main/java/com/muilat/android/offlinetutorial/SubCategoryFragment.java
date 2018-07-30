package com.muilat.android.offlinetutorial;

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

import static com.muilat.android.offlinetutorial.data.SubCategories.getSubCatByCatId;

public class SubCategoryFragment extends Fragment implements  LoaderManager.LoaderCallbacks<ArrayList<SubCategories>>  {

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
    public Loader<ArrayList<SubCategories>> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<ArrayList<SubCategories>>(getActivity()) {

            // Initialize a ArrayList<SubCategories>, this will hold all the Category data
            ArrayList<SubCategories> mSubCategorysData = null;

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
            public ArrayList<SubCategories> loadInBackground() {
                String selection = null;
                String[] selectionArgs = null;
                if(category_id > 0){
                    selection = "category_id=?";
                    selectionArgs = new String[]{String.valueOf((category_id))};
                }

                try {
//                    return getActivity().getContentResolver().query(CONTENT_URI,
//                            null,
//                            selection,
//                            selectionArgs,
//                            null);
                    return getSubCatByCatId(category_id);
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage()+" Failed to asynchronously load Categorys.");
                    e.printStackTrace();
                    return null;
                }
            }

            public void deliverResult(ArrayList<SubCategories> data) {
                mSubCategorysData = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<SubCategories>> loader, ArrayList<SubCategories> data) {
        mSubCategoryAdapter.swapCursor(data);
//        Log.e(TAG, "No of cursor: "+data.getCount());
        Log.e(TAG, "No of Subcategories: "+data.size());
        Log.e(TAG, "No of Adapte item: "+mSubCategoryAdapter.getItemCount());


    }

    @Override
    public void onLoaderReset(Loader<ArrayList<SubCategories>> loader) {
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
