package com.muilat.android.offlinetutorial;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.muilat.android.offlinetutorial.adapter.LessonViewAdapter;
import com.muilat.android.offlinetutorial.data.Lessons;

import java.util.ArrayList;

public class FavouriteViewActivity extends AppCompatActivity {

    private static final String TAG = FavouriteViewActivity.class.getSimpleName();

    public static final String EXTRA_POSITION = "extra-position";
    public static final String EXTRA_FAVOURITES = "extra-favourites";

    ArrayList<Lessons> mFavourite = new ArrayList<>();
    static int mPosition = -1;

    static LessonViewAdapter mLessonViewAdapter;
    RecyclerView recycler;

    Button nextButton, previousButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_view);

        nextButton = findViewById(R.id.next_button);
        previousButton = findViewById(R.id.previous_button);

        if(getIntent() != null){
            mFavourite = getIntent().getParcelableArrayListExtra(EXTRA_FAVOURITES);
            mPosition = getIntent().getIntExtra(EXTRA_POSITION, -1);
        }


        mLessonViewAdapter = new LessonViewAdapter();

        recycler =  findViewById(R.id.lessons_recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false);
        recycler.setLayoutManager(layoutManager);

        recycler.setItemAnimator(new DefaultItemAnimator()); //Animator for recycler view
        recycler.setAdapter(mLessonViewAdapter);
        recycler.setHasFixedSize(true);

        mLessonViewAdapter.swapCursor(mFavourite);

        recycler.scrollToPosition(mPosition);


        recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE){
                    mPosition = getCurrentPosition();
                    //while showing another lesson, check if its favourite
//                    Lessons lesson = mLessonViewAdapter.getItem(mPosition);
//                    if (lesson.isFavourite()){
//                        LessonActivity.favouriteIcon.setImageResource(R.drawable.ic_favorite_white_24dp);
//                    }
//                    else
//                        LessonActivity.favouriteIcon.setImageResource(R.drawable.ic_favorite_border_white_24dp);

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
            @RequiresApi(api = Build.VERSION_CODES.O)
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

}
