package com.muilat.android.offlinetutorial.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.muilat.android.offlinetutorial.QuizActivity;
import com.muilat.android.offlinetutorial.R;
import com.muilat.android.offlinetutorial.data.QuizSet;
import com.muilat.android.offlinetutorial.util.Utils;

public class QuizSetAdapter  extends RecyclerView.Adapter<QuizSetAdapter.ViewHolder> {

    //    private Cursor mCursor;
    Cursor mCursor;
    private final String TAG = QuizSetAdapter.class.getSimpleName();

    Context mContext;

    //    String user_lang_pref;
    static Drawable d;



    @Override
    public QuizSetAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        final  ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.quiz_set_item, parent, false));

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int position = viewHolder.getAdapterPosition();
                QuizSet quizSet = getItem(position);

                Intent intent = new Intent(mContext, QuizActivity.class);
                intent.putExtra(QuizActivity.EXTRA_QUIZ_SET_ID, quizSet.getId());
                mContext.startActivity(intent);


            }
        });

        d = ContextCompat.getDrawable(mContext,R.drawable.first_letter_circle);


        return viewHolder;
    }

    @Override
    @TargetApi(Build.VERSION_CODES.N)
    public void onBindViewHolder(QuizSetAdapter.ViewHolder holder, int position) {

        mCursor.moveToPosition(position); // get to the right location in the cursor

        QuizSet quizSet = new QuizSet(mCursor);
        holder.name.setText(quizSet.getName());
        d.setColorFilter(Utils.generateColor(), PorterDuff.Mode.DARKEN);

        holder.quizSet_cardView.setTag(quizSet);

    }

    @Override
    public int getItemCount() {
        if (mCursor == null) {
            return 0;
        }
        return mCursor.getCount();
    }

    /**
     * Return a {@link QuizSet} represented by this item in the adapter.
     * Method is used to run machine tests.
     *
     * @param position Cursor item position
     * @return A new {@link QuizSet}
     */
    public QuizSet getItem(int position) {
        if (mCursor.moveToPosition(position)) {
            return new QuizSet(mCursor);
        }

        return null;
    }

    /**
     * @param data update cursor
     */
    public void swapCursor(Cursor data) {
        Log.e(TAG, "Swapping QuizSet");
        mCursor = data;
        notifyDataSetChanged();
    }

    /**
     * An Recycler item view
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView name;
//        public ImageView icon;
//        public LinearLayout iconLayout;
        CardView quizSet_cardView;


        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.quiz_set_name);
//            icon = itemView.findViewById(R.id.quiz_set_icon);
//            iconLayout = itemView.findViewById(R.id.quiz_set_icon_layout);
            quizSet_cardView = itemView.findViewById(R.id.quiz_set_card);

        }
    }
}
