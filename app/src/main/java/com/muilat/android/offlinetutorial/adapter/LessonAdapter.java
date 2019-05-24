package com.muilat.android.offlinetutorial.adapter;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.muilat.android.offlinetutorial.FavouriteActivity;
import com.muilat.android.offlinetutorial.LessonActivity;
import com.muilat.android.offlinetutorial.LessonViewFragment;
import com.muilat.android.offlinetutorial.R;
import com.muilat.android.offlinetutorial.data.Lessons;
import com.muilat.android.offlinetutorial.data.OfflineTutorialContract;
import com.muilat.android.offlinetutorial.util.Utils;

import java.util.ArrayList;


public class LessonAdapter extends RecyclerView.Adapter<LessonAdapter.ViewHolder> {

    //    private ArrayList<Lessons> mCursor;
//    ArrayList<Lessons> mLessonArrayList;
    private final String TAG = LessonAdapter.class.getSimpleName();

    Context mContext;

    ArrayList<Lessons> lessonsArrayList;

//    String user_lang_pref;
    static Drawable d;





    @Override
    public LessonAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        final  ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.lesson_item, parent, false));




        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int position = viewHolder.getAdapterPosition();
//                Lessons lesson = getItem(position);

                    loadLessonView(position);


            }
        });
        d = ContextCompat.getDrawable(mContext,R.drawable.first_letter_circle);

        return viewHolder;
    }

    @Override
    @TargetApi(Build.VERSION_CODES.N)
    public void onBindViewHolder(LessonAdapter.ViewHolder holder, final int position) {

        //if cursor is one goto lessonview
        if(lessonsArrayList.size() == 1){
            if(mContext instanceof LessonActivity)
                loadLessonView(position);
        }



        final Lessons lesson = lessonsArrayList.get(position);


//        lessonsArrayList.add(lesson);


        d.setColorFilter(Utils.generateColor(), PorterDuff.Mode.DARKEN);

        holder.icon.setBackground(d);
        holder.title.setText(lesson.getTitle());
        holder.icon.setText(position+1+"");

        String des = lesson.getDescription();
        if(des.length() > 60){
            des = des.substring(0,57)+"...";
        }

        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.N){

            holder.description.setText(Html.fromHtml(des));


        }
        else {
            holder.description.setText(Html.fromHtml(des,Html.FROM_HTML_MODE_COMPACT));
        }

        holder.word_cardView.setTag(lesson.getID());


        if (mContext instanceof FavouriteActivity){
            holder.favourite.setVisibility(View.VISIBLE);

            holder.favourite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    removeReaddFav(view, lesson, position);


                }
            });
        }



//        Log.e(TAG, lesson.getTitle()+" is here");

    }

    private void removeReaddFav(View view, final Lessons lesson, final int position) {
        //remove from favoirites
        String stringId = Long.toString(lesson.getID());
        Uri uri = OfflineTutorialContract.LessonEntry.CONTENT_URI;
        final Uri appended_uri = uri.buildUpon().appendPath(stringId).build();

        final ContentValues contentValues = new ContentValues();
        contentValues.put(OfflineTutorialContract.LessonEntry.COLUMN_IS_FAVOURITE, 0);
        mContext.getContentResolver().update(appended_uri,contentValues,null,null);

        /*This removes the item from the recycler view*/
        lessonsArrayList.remove(lesson);
        if (lessonsArrayList.isEmpty()){
            FavouriteActivity.favouriteEmpty();
        }
        notifyDataSetChanged();

        Snackbar snackbar = Snackbar.make(view,lesson.getTitle()+ " deleted from favorites!", Snackbar.LENGTH_LONG);
        snackbar.setAction("UNDO", new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //TODO:resave the item to db
                contentValues.put(OfflineTutorialContract.LessonEntry.COLUMN_IS_FAVOURITE, 1);
                mContext.getContentResolver().update(appended_uri,contentValues,null,null);
                lessonsArrayList.add(position, lesson);
                notifyDataSetChanged();
            }
        });
        snackbar.setActionTextColor(mContext.getResources().getColor(R.color.colorCorrect));
        snackbar.show();
    }

    private void loadLessonView(int position) {

        Intent lessonViewIntent = new Intent(mContext, LessonViewFragment.class);
        lessonViewIntent.putParcelableArrayListExtra(LessonViewFragment.ARG_LESSONS,lessonsArrayList);
        lessonViewIntent.putExtra(LessonViewFragment.ARG_LESSON_POSITION,position);
        mContext.startActivity(lessonViewIntent);

    }

    @Override
    public int getItemCount() {
        if (lessonsArrayList == null) {
            return 0;
        }
        return lessonsArrayList.size();
    }

    /**
     * Return a {@link Lessons} represented by this item in the adapter.
     * Method is used to run machine tests.
     *
     * @param position ArrayList<Lessons> item position
     * @return A new {@link Lessons}
     */
    public Lessons getItem(int position) {
        if(position < lessonsArrayList.size() || position > lessonsArrayList.size()){
            return lessonsArrayList.get(position);
        }
        return null;
    }

    /**
     * @param data update cursor
     */
    public void swapCursor(ArrayList<Lessons> data) {
        Log.e(TAG, "Swapping Lessons");
        lessonsArrayList = data;
        notifyDataSetChanged();
    }

    /**
     * An Recycler item view
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView title, description, icon;
        CardView word_cardView;
        public ImageView favourite;


        public ViewHolder(View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.lesson_icon);
            title = itemView.findViewById(R.id.lesson_title);
            description = itemView.findViewById(R.id.lesson_description);
            favourite = itemView.findViewById(R.id.favourite);
            word_cardView = itemView.findViewById(R.id.lesson_list_card);



        }
    }


}
