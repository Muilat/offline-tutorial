package com.muilat.android.offlinetutorial;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.muilat.android.offlinetutorial.data.LessonAdapter;
import com.muilat.android.offlinetutorial.data.Lessons;
import com.muilat.android.offlinetutorial.data.OfflineTutorialContract;
import com.muilat.android.offlinetutorial.data.SubCategories;

import java.util.ArrayList;

public class LessonActivity extends AppCompatActivity{
    public static final String EXTRA_SUB_CATEGORY = "extra_sub_category";


        public static FragmentTransaction fragmentTransaction;
    public static FragmentManager fragmentManager;

    SubCategories sub_category;

    public static ImageView favouriteIcon, copyIcon, searchIcon, shareIcon;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar toolbar;
        toolbar = findViewById(R.id.toolbar);
        setContentView(R.layout.activity_details);

        TextView toolbar_title = findViewById(R.id.toolbar_title);
        favouriteIcon = findViewById(R.id.favourite);
        copyIcon = findViewById(R.id.content_copy);
        searchIcon = findViewById(R.id.action_search);
        shareIcon = findViewById(R.id.share);




        if (getIntent() != null) {
            sub_category = getIntent().getParcelableExtra(EXTRA_SUB_CATEGORY);

            LessonListFragment lessonListFragment = new LessonListFragment();
            Bundle args = new Bundle();
            args.putInt(LessonListFragment.ARG_SUB_CATEGORY_ID,sub_category.getId());
            lessonListFragment.setArguments(args);

            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();

            fragmentTransaction.replace(R.id.lesson_container,lessonListFragment );
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }

        toolbar_title.setText(sub_category.getTitle());
        setSupportActionBar(toolbar);

        favouriteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: go get the lesson
                Lessons lesson = LessonViewFragment.getLessonInViewId();

                String stringId = Long.toString(lesson.getID());
                Uri uri = OfflineTutorialContract.LessonEntry.CONTENT_URI;
                uri = uri.buildUpon().appendPath(stringId).build();

                ContentValues contentValues = new ContentValues();
                if(lesson.isFavourite()){
                    contentValues.put(OfflineTutorialContract.LessonEntry.COLUMN_IS_FAVOURITE, 0);
                    Toast.makeText(LessonActivity.this, lesson.getTitle()+" is removed to favourite", Toast.LENGTH_SHORT).show();
                    favouriteIcon.setImageResource(R.drawable.ic_favorite_border_white_24dp);


                }
                else {
                    contentValues.put(OfflineTutorialContract.LessonEntry.COLUMN_IS_FAVOURITE, 1);
                    Toast.makeText(LessonActivity.this, lesson.getTitle()+" is added to favourite", Toast.LENGTH_SHORT).show();
                    favouriteIcon.setImageResource(R.drawable.ic_favorite_white_24dp);

                }
                getContentResolver().update(uri,contentValues,null,null);


            }
        });

        copyIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Lessons lesson = LessonViewFragment.getLessonInViewId();

                String textToCopy = lesson.getTitle()+"\n"+lesson.getDescription();
                if(Build.VERSION.SDK_INT<Build.VERSION_CODES.HONEYCOMB){
                    android.text.ClipboardManager clipboardManager = (android.text.ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
                    clipboardManager.setText(textToCopy);
                }
                else {
                    android.content.ClipboardManager clipboardManager = (android.content.ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
                    android.content.ClipData clipData = ClipData.newPlainText("Copied text" ,textToCopy);
                    clipboardManager.setPrimaryClip(clipData);
                }
                Toast.makeText(LessonActivity.this, lesson.getTitle()+" copied to clipboard", Toast.LENGTH_SHORT).show();

            }
        });


    }

    public void onArrowBackClick(View view){
        //todo: dont close, go to previous if available
//        fragmentTransaction.hide()
        finish();


    }

    @Override
    public void onBackPressed() {
//            if ()) {
//                drawer.closeDrawer(GravityCompat.START);
//            } else {
//        super.onBackPressed();
//            }
        finish();
    }

    public void onSearchClick(View view){

    }


}
