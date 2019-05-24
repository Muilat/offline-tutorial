package com.muilat.android.offlinetutorial.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import com.muilat.android.offlinetutorial.R;
import com.muilat.android.offlinetutorial.data.Lessons;

import java.util.ArrayList;

public class LessonViewAdapter extends RecyclerView.Adapter<LessonViewAdapter.ViewHolder> {

    //    private ArrayList<Lessons> mlessonArrayList;
    ArrayList<Lessons> mlessonArrayList;
    private final String TAG = LessonViewAdapter.class.getSimpleName();

    Context mContext;

    String text_size_pref;

    LessonViewAdapter.ViewHolder viewHolder;


    @Override
    public LessonViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        final ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.lesson_view, parent, false));

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int position = viewHolder.getAdapterPosition();
                Lessons lesson = getItem(position);

                //TODO: show single lesson item


            }
        });

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        text_size_pref = sharedPreferences.getString(mContext.getString(R.string.pref_text_size_key),
                mContext.getResources().getString(R.string.pref_text_size_small_value));



        return viewHolder;
    }

    @Override
    @TargetApi(Build.VERSION_CODES.N)
    public void onBindViewHolder(LessonViewAdapter.ViewHolder holder, int position) {


        Lessons lesson = mlessonArrayList.get(position);

        viewHolder = holder;


//        holder.name.setBackgroundColor(word.getColorInt());
        holder.title.setText(lesson.getTitle());
//        holder.title.setBackgroundColor(ColorUtil.generateColor());

//        holder.icon.setText(position+"");
//        holder.description.setText(lesson.getDescription());
        holder.webView.loadData(lesson.getDescription(),"text/html; charset=utf-8",null);

        if (text_size_pref.equals(mContext.getResources().getString(R.string.pref_text_size_large_value))) {
            setTextSize((int) mContext.getResources().getDimension(R.dimen.text_large));

        } else if (text_size_pref.equals(mContext.getResources().getString(R.string.pref_text_size_medium_value))) {
            setTextSize((int) mContext.getResources().getDimension(R.dimen.text_medium));

        } else {
            setTextSize((int) mContext.getResources().getDimension(R.dimen.text_small));

        }

//        Log.e(TAG, lesson.getTitle()+" is here");

    }

    @Override
    public int getItemCount() {
        if (mlessonArrayList == null) {
            return 0;
        }
        return mlessonArrayList.size();
    }

    /**
     * Return a {@link com.muilat.android.offlinetutorial.data.Lessons} represented by this item in the adapter.
     * Method is used to run machine tests.
     *
     * @param position ArrayList<Lessons> item position
     * @return A new {@link com.muilat.android.offlinetutorial.data.Lessons}
     */
    public Lessons getItem(int position) {
//        if (mlessonArrayList.moveToPosition(position)) {
//        return new Lessons(mlessonArrayList);
//        }

        if (position < mlessonArrayList.size() || position > mlessonArrayList.size()) {
            return mlessonArrayList.get(position);
        }

        return null;
    }

    /**
     * @param data update cursor
     */
    public void swapCursor(ArrayList<Lessons> data) {
        Log.e(TAG, "Swapping Lesson views");
        mlessonArrayList = data;
        notifyDataSetChanged();
    }

    /**
     * An Recycler item view
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView title, description;
        CardView word_cardView;

        WebView webView;
        WebSettings settings;


        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.lesson_title);
//            description = itemView.findViewById(R.id.lesson_description);
            webView = itemView.findViewById(R.id.webView);
            settings = webView.getSettings();
            settings.setDefaultTextEncodingName("utf-8");



        }
    }

    public void setTextSize(int size) {

        viewHolder.title.setTextSize(size);
//        viewHolder.description.setTextSize(size);
        viewHolder.settings.setDefaultFontSize(size);
    }
}
