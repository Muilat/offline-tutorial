package com.muilat.android.offlinetutorial.data;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.muilat.android.offlinetutorial.R;

import java.util.ArrayList;

public class WordAdapter  extends RecyclerView.Adapter<WordAdapter.ViewHolder> {

    //    private Cursor mCursor;
    ArrayList<Words> mWords;
    private final String TAG = WordAdapter.class.getSimpleName();

    Context mContext;

//    String user_lang_pref;



    @Override
    public WordAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        final  ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.word_item, parent, false));

//        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                int position = viewHolder.getAdapterPosition();
//                Words word = getItem(position);
//
//
//            }
//        });

        return viewHolder;
    }

    @Override
    @TargetApi(Build.VERSION_CODES.N)
    public void onBindViewHolder(WordAdapter.ViewHolder holder, int position) {

//        mCursor.moveToPosition(position); // get to the right location in the cursor

//        Words word = new Words(mCursor);
        Words word = mWords.get(position);

//        if(user_lang_pref.equals(mContext.getResources().getString(R.string.pref_lang_hau_yor_value))){
//            holder.name.setText(word.getHausa()+"");
//
//        }else {
//            holder.name.setText(word.getName()+"");
//        }
//        holder.name.setBackgroundColor(word.getColorInt());
        holder.title.setText(word.getTitle());
        holder.description.setText(word.getDescription());
        holder.word_cardView.setTag(word.getSubCategoryID());

        Log.e(TAG, word.getTitle()+" is here");

    }

    @Override
    public int getItemCount() {
        if (mWords == null) {
            return 0;
        }
        return mWords.size();
    }

    /**
     * Return a {@link Words} represented by this item in the adapter.
     * Method is used to run machine tests.
     *
     * @param position Cursor item position
     * @return A new {@link Words}
     */
    public Words getItem(int position) {
//        if (mCursor.moveToPosition(position)) {
//            return new Words(mCursor);
//        }
        if(position > -1 && position < getItemCount()){
            return mWords.get(position);
        }
        return null;
    }

    /**
     * @param data update cursor
     */
    public void swapCursor(ArrayList<Words> data) {
        Log.e(TAG, "Swapping Words");
        mWords = data;
        notifyDataSetChanged();
    }

    /**
     * An Recycler item view
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView title, description;
        CardView word_cardView;


        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.word_title);
            description = itemView.findViewById(R.id.word_description);
            word_cardView = itemView.findViewById(R.id.word_card);



        }
    }
}
