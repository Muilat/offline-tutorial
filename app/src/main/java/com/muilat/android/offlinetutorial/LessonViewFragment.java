package com.muilat.android.offlinetutorial;

import android.content.ClipData;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.muilat.android.offlinetutorial.adapter.LessonViewAdapter;
import com.muilat.android.offlinetutorial.data.Lessons;
import com.muilat.android.offlinetutorial.data.OfflineTutorialContract;

import java.util.ArrayList;

public class LessonViewFragment extends AppCompatActivity {

    private static final String TAG = LessonViewFragment.class.getName();
    public static final String ARG_LESSON_POSITION = "lesson_positon";
    public static final String ARG_LESSONS = "lessons";
    public static final String ARG_SINGLE_LESSON = "lesson";

    private static LessonViewAdapter mLessonViewAdapter;

    RecyclerView recycler;
    ImageView favouriteIcon, copyIcon, searchIcon, shareIcon;
    TextView total,number;
    Button nextButton, previousButton;


    public ArrayList<Lessons> mLesson = new ArrayList<>();
    static int mPosition = 0;


    public LessonViewFragment(){
        //the empty constructor
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        copyIcon = findViewById(R.id.content_copy);
        searchIcon = findViewById(R.id.action_search);
        shareIcon = findViewById(R.id.share);
        favouriteIcon = findViewById(R.id.favourite);
        total = findViewById(R.id.lesson_total);
        number = findViewById(R.id.lesson_number);

        TextView toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText("Lesson Detail");

        if(savedInstanceState != null){
            mLesson = savedInstanceState.getParcelableArrayList(ARG_LESSONS);
            mPosition = savedInstanceState.getInt(ARG_LESSON_POSITION);
        }
        else if(getIntent() != null) {
            mLesson = getIntent().getParcelableArrayListExtra(ARG_LESSONS);
            mPosition = getIntent().getIntExtra(ARG_LESSON_POSITION, mPosition);
        }

        mLessonViewAdapter = new LessonViewAdapter();

        recycler =  findViewById(R.id.lessons_recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        recycler.setLayoutManager(layoutManager);

        recycler.setItemAnimator(new DefaultItemAnimator()); //Animator for recycler view
        recycler.setAdapter(mLessonViewAdapter);
        recycler.setHasFixedSize(true);

        mLessonViewAdapter.swapCursor(mLesson);

        recycler.scrollToPosition(mPosition);
        checkFavourite(mPosition);
        number.setText(mPosition+1+"");
        total.setText(mLesson.size()+"");


        recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE){
                    mPosition = getCurrentPosition();

                    //while showing another lesson, check if its favourite
                    checkFavourite(mPosition);
                    number.setText(mPosition+1+"");

                }
            }
        });


        nextButton = findViewById(R.id.next_button);
        previousButton = findViewById(R.id.previous_button);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RecyclerView.Adapter adapter = recycler.getAdapter();
                if(adapter == null)
                    return;
                mPosition = getCurrentPosition();
                int count = adapter.getItemCount();
                if(mPosition<(count - 1))
                    mPosition++;
                setCurrentPosition(mPosition, true);

            }
        });

        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPosition = getCurrentPosition();
                if (mPosition > 0){
                    mPosition--;
                    setCurrentPosition(mPosition, true);
                }
            }
        });


        PagerSnapHelper pagerSnapHelper = new PagerSnapHelper();
        pagerSnapHelper.attachToRecyclerView(recycler);


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
                    Toast.makeText(LessonViewFragment.this, lesson.getTitle()+" is removed from favourite", Toast.LENGTH_SHORT).show();
                    favouriteIcon.setImageResource(R.drawable.ic_favorite_border_white_24dp);
                    lesson.setIsFavourite("0");

                }
                else {
                    contentValues.put(OfflineTutorialContract.LessonEntry.COLUMN_IS_FAVOURITE, 1);
                    Toast.makeText(LessonViewFragment.this, lesson.getTitle()+" is added to favourite", Toast.LENGTH_SHORT).show();
                    favouriteIcon.setImageResource(R.drawable.ic_favorite_white_24dp);
                    lesson.setIsFavourite("1");

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
                Toast.makeText(LessonViewFragment.this, lesson.getTitle()+" copied to clipboard", Toast.LENGTH_SHORT).show();

            }
        });

        shareIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Lessons lesson = LessonViewFragment.getLessonInViewId();
                String textToShare = lesson.getTitle()+"\n"+lesson.getDescription();
                textToShare += "\nInstall from http://play.google.com/store/apps/details?id=" + getPackageName();

                Intent shareIntent = ShareCompat.IntentBuilder.from(LessonViewFragment.this)
                        .setText(textToShare)
                        .setChooserTitle("Share Developer with")
                        .setSubject(getString(R.string.app_name))
                        .setType("text/plain")
                        .createChooserIntent();

                if(shareIntent.resolveActivity(getPackageManager()) != null){
                    startActivity(shareIntent);
                }
            }
        });

    }

    private void checkFavourite(int position) {
        Lessons lesson = mLessonViewAdapter.getItem(position);
        Log.e(TAG, lesson.mIsFavourite);
        if (lesson.isFavourite()){
            favouriteIcon.setImageResource(R.drawable.ic_favorite_white_24dp);
        }
        else
            favouriteIcon.setImageResource(R.drawable.ic_favorite_border_white_24dp);
    }


    public int getCurrentPosition(){
        return ((LinearLayoutManager)recycler.getLayoutManager())
                .findFirstVisibleItemPosition();
    }

    public  void setCurrentPosition(int position, boolean smooth){
        if(smooth)
            recycler.smoothScrollToPosition(position);
        else
            recycler.scrollToPosition(position);


    }

    public static Lessons getLessonInViewId(){
        Lessons lesson = mLessonViewAdapter.getItem(mPosition);

        return lesson;
    }

    public void onArrowBackClick(View view){
        finish();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(ARG_LESSON_POSITION, mPosition);
        outState.putParcelableArrayList(ARG_LESSONS, mLesson);
        super.onSaveInstanceState(outState);
    }
}
