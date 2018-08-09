package com.muilat.android.offlinetutorial;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.muilat.android.offlinetutorial.adapter.CategoryAdapter;
import com.muilat.android.offlinetutorial.data.OfflineTutorialContract;

public class CategoryFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor>  {

    private static final int CATEGORY_LOADER_ID = 1111;
    private static final String TAG = CategoryFragment.class.getName();
    private CategoryAdapter mCategoryAdapter;

    boolean data_fetched = false;


    public CategoryFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }

        if(savedInstanceState == null){
            //Ensure a loader is initialized and active
            getActivity().getSupportLoaderManager().initLoader(CATEGORY_LOADER_ID, null, this);

        }else {
            if(!savedInstanceState.getBoolean("data_fetched")){
                getActivity().getSupportLoaderManager().initLoader(CATEGORY_LOADER_ID, null, this);

            }
        }

     }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_category, container, false);

        RecyclerView recycler =  view.findViewById(R.id.category_recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recycler.setLayoutManager(layoutManager);

        mCategoryAdapter = new CategoryAdapter();
        recycler.setAdapter(mCategoryAdapter);
        recycler.setHasFixedSize(true);

        return view;
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<Cursor>(getActivity()) {

            // Initialize a Cursor, this will hold all the Category data
//            Cursor mCategoriesData = null;
            Cursor mCategoriesData;

            // onStartLoading() is called when a loader first starts loading data
            @Override
            protected void onStartLoading() {
                if (mCategoriesData != null) {
                    data_fetched = true;
                    // Delivers any previously loaded data immediately
                    deliverResult(mCategoriesData);
                } else {
                    // Force a new load
                    forceLoad();
                }
            }

            // loadInBackground() performs asynchronous loading of data
            @Override
            public Cursor loadInBackground() {

                try {
                    return getActivity().getContentResolver().query(OfflineTutorialContract.CategoryEntry.CONTENT_URI,
                            null,
                            null,
                            null,
                            null);
//                    return Categories.dummyCategories();
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage()+" Failed to asynchronously load Categorys.");
                    e.printStackTrace();
                    return null;
                }
            }

            public void deliverResult(Cursor data) {
                mCategoriesData = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCategoryAdapter.swapCursor(data);
        data_fetched = true;

//        Log.e(TAG, "No of cursor: "+data.getCount());
        Log.e(TAG, "No of Categories: "+data.getCount());


    }

    @Override
    public void onLoaderReset(Loader<Cursor>loader) {
        mCategoryAdapter.swapCursor(null);
    }

    //on category click
//    public void onCategoryClicked(View view){
////        subCat_holder_linearLayout = view.findViewById(R.id.sub_cat_holder);
//        subCat_recyclerView = view.findViewById(R.id.sub_cat_recyclerView);
//        int category_id = (int) subCat_holder_linearLayout.getTag();
//
//        if(selected_category !=null){
//            //a feeling alredy selected, deselect
//            selected_category.setVisibility(View.GONE);
//        }
//
//        LinearLayout selected = view.findViewById(R.id.sub_cat_holder);
//        selected.setVisibility(View.VISIBLE);
//
//        selected_category = selected;
//
//        //Get subcategories
//        SubCategoryFragment commonFragment = new SubCategoryFragment();
//        Bundle args = new Bundle();
//        args.putInt(SubCategoryFragment.ARG_CATEGORY_ID,category_id);
//        commonFragment.setArguments(args);
//
//        fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.sub_cat_holder,commonFragment );
//        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
//        fragmentTransaction.addToBackStack(null);
//        fragmentTransaction.commit();
//
//
//    }

    @Override
    public void onResume() {
        super.onResume();

        // re-queries for all Categories
//        getActivity().getSupportLoaderManager().restartLoader(CATEGORY_LOADER_ID, null, this);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("data_fetched", data_fetched);
        super.onSaveInstanceState(outState);
    }


}