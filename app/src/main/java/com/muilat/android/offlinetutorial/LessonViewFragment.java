package com.muilat.android.offlinetutorial;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.muilat.android.offlinetutorial.adapter.LessonViewAdapter;
import com.muilat.android.offlinetutorial.data.LessonAdapter;
import com.muilat.android.offlinetutorial.data.Lessons;

import java.util.ArrayList;

import static com.muilat.android.offlinetutorial.SubCategoryFragment.ARG_CATEGORY_ID;

public class LessonViewFragment extends Fragment {

    private static final String TAG = CategoryFragment.class.getName();
    public static final String ARG_LESSON_POSITION = "lesson_positon";
    public static final String ARG_LESSONS = "lessons";
    public static final String ARG_SINGLE_LESSON = "lesson";

    private static LessonViewAdapter mLessonViewAdapter;
    RecyclerView recycler;

    LinearLayout buttonLinearLayout;
    Button nextButton, previousButton;


    public ArrayList<Lessons> mLesson = new ArrayList<>();
    static int mPosition = 0;


    public LessonViewFragment(){
        //the empty constructor
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Bundle bundle = getArguments();
            mLesson = bundle.getParcelableArrayList(ARG_LESSONS);
            mPosition = bundle.getInt(ARG_LESSON_POSITION);
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_lessons, container, false);

        //make copy and favourite Visible
        LessonActivity.favouriteIcon.setVisibility(View.VISIBLE);
        LessonActivity.searchIcon.setVisibility(View.GONE);
        LessonActivity.copyIcon.setVisibility(View.VISIBLE);
        LessonActivity.shareIcon.setVisibility(View.VISIBLE);

        mLessonViewAdapter = new LessonViewAdapter();

        recycler =  view.findViewById(R.id.lessons_recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        recycler.setLayoutManager(layoutManager);

        recycler.setItemAnimator(new DefaultItemAnimator()); //Animator for recycler view
        recycler.setAdapter(mLessonViewAdapter);
        recycler.setHasFixedSize(true);

        mLessonViewAdapter.swapCursor(mLesson);

        recycler.scrollToPosition(mPosition);

        buttonLinearLayout = view.findViewById(R.id.nav_button_layout);
        buttonLinearLayout.setVisibility(View.VISIBLE);

        recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE){
                    mPosition = getCurrentPosition();
                    //while showing another lesson, check if its favourite
                    Lessons lesson = mLessonViewAdapter.getItem(mPosition);
                    if (lesson.isFavourite()){
                        LessonActivity.favouriteIcon.setImageResource(R.drawable.ic_favorite_white_24dp);
                    }
                    else
                        LessonActivity.favouriteIcon.setImageResource(R.drawable.ic_favorite_border_white_24dp);

                }
            }
        });


        nextButton = view.findViewById(R.id.next_button);
        previousButton = view.findViewById(R.id.previous_button);

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

        return view;
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


//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnAddGigOverviewFragmentContinueListener) {
//            mListener = (OnAddGigOverviewFragmentContinueListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }
//
//    /**
//     * This interface must be implemented by activities that contain this
//     * fragment to allow an interaction in this fragment to be communicated
//     * to the activity and potentially other fragments contained in that
//     * activity.
//     */
//    public interface OnButtonClickListener {
//        void onButtonClick(int position);
//    }



}
