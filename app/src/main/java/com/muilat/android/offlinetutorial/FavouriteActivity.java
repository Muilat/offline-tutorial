package com.muilat.android.offlinetutorial;

import android.app.Dialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.muilat.android.offlinetutorial.adapter.LessonAdapter;
import com.muilat.android.offlinetutorial.data.Lessons;
import com.muilat.android.offlinetutorial.data.OfflineTutorialContract;

import java.util.ArrayList;

public class FavouriteActivity extends AppCompatActivity  implements
        LoaderManager.LoaderCallbacks<Cursor>  {

    private static final int FAVOURITE_LOADER_ID = 775;
    private static final String TAG = FavouriteActivity.class.getSimpleName();
    LessonAdapter mFavouriteAdapter;

    ArrayList<Lessons> mFavouriteArrayList = new ArrayList<>();
    RecyclerView recycler;
    static ImageView delete_all, closeDialog;
    static LinearLayout emptyView;

    Button confrimDelete;
    Dialog deleteAllDialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);

        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText(getString(R.string.favourites));
        setSupportActionBar(toolbar);

        delete_all = findViewById(R.id.delete_all);
        emptyView = findViewById(R.id.emptyView);
        mFavouriteAdapter = new LessonAdapter();


        recycler =  findViewById(R.id.favourite_recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        recycler.setLayoutManager(layoutManager);

        recycler.setItemAnimator(new DefaultItemAnimator()); //Animator for recycler view
        recycler.setAdapter(mFavouriteAdapter);
        recycler.setHasFixedSize(true);

        getSupportLoaderManager().initLoader(FAVOURITE_LOADER_ID, null, this);


        delete_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteAllDialog = new Dialog(FavouriteActivity.this);

                deleteAllDialog.setContentView(R.layout.negative_dialog);
                closeDialog = deleteAllDialog.findViewById(R.id.close_dialog);
                confrimDelete = deleteAllDialog.findViewById(R.id.confirm);

                closeDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        deleteAllDialog.dismiss();
                    }
                });

                confrimDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        for(Lessons lesson: mFavouriteArrayList){
                            String stringId = Long.toString(lesson.getID());
                            Uri uri = OfflineTutorialContract.LessonEntry.CONTENT_URI;
                            uri = uri.buildUpon().appendPath(stringId).build();
                            ContentValues contentValues = new ContentValues();
                            contentValues.put(OfflineTutorialContract.LessonEntry.COLUMN_IS_FAVOURITE, 0);
                            lesson.setIsFavourite("0");
                            getContentResolver().update(uri,contentValues,null,null);

                        }
                        Toast.makeText(FavouriteActivity.this, "Favourites deleted!", Toast.LENGTH_SHORT).show();
                        deleteAllDialog.dismiss();
                        favouriteEmpty();
                    }
                });

                deleteAllDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                deleteAllDialog.show();
            }
        });

//        mFavouriteAdapter.

        recycler.setRecyclerListener(new RecyclerView.RecyclerListener() {
            @Override
            public void onViewRecycled(RecyclerView.ViewHolder holder) {

            }
        });

//        recycler.onChan


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
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage()+" Failed to asynchronously load favourites.");
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

        if(data.getCount()==0){
            favouriteEmpty();
        }

    }


    @Override
    public void onLoaderReset(Loader<Cursor>loader) {
        mFavouriteAdapter.swapCursor(null);
    }

    public static void favouriteEmpty() {
        emptyView.setVisibility(View.VISIBLE);
        delete_all.setVisibility(View.GONE);
    }

    public void onArrowBackClick(View view){
        finish();

    }


}
