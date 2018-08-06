package com.muilat.android.offlinetutorial.data;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.muilat.android.offlinetutorial.FavouriteActivity;
import com.muilat.android.offlinetutorial.FavouriteViewActivity;
import com.muilat.android.offlinetutorial.LessonActivity;
import com.muilat.android.offlinetutorial.LessonViewFragment;
import com.muilat.android.offlinetutorial.R;

import java.util.ArrayList;

import static com.muilat.android.offlinetutorial.LessonActivity.fragmentManager;

public class LessonAdapter extends RecyclerView.Adapter<LessonAdapter.ViewHolder> {

    //    private ArrayList<Lessons> mCursor;
//    ArrayList<Lessons> mLessonArrayList;
    private final String TAG = LessonAdapter.class.getSimpleName();

    Context mContext;

    ArrayList<Lessons> lessonsArrayList;

//    String user_lang_pref;
FragmentTransaction fragmentTransaction;




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

        return viewHolder;
    }

    @Override
    @TargetApi(Build.VERSION_CODES.N)
    public void onBindViewHolder(LessonAdapter.ViewHolder holder, int position) {

        //if cursor is one goto lessonview
        if(lessonsArrayList.size() == 1){
            if(mContext instanceof LessonActivity)
                loadLessonView(position);
        }



        final Lessons lesson = lessonsArrayList.get(position);


//        lessonsArrayList.add(lesson);



//        holder.name.setBackgroundColor(word.getColorInt());
        holder.title.setText(lesson.getTitle());
        holder.icon.setText(position+1+"");
        if(lesson.getDescription().length() >50){
            holder.description.setText(lesson.getDescription().substring(0,47)+"...");

        }
        holder.word_cardView.setTag(lesson.getID());


        if (mContext instanceof FavouriteActivity){
            holder.favourite.setVisibility(View.VISIBLE);

            holder.favourite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //remove from favoirites
                    String stringId = Long.toString(lesson.getID());
                    Uri uri = OfflineTutorialContract.LessonEntry.CONTENT_URI;
                    uri = uri.buildUpon().appendPath(stringId).build();

                    ContentValues contentValues = new ContentValues();
                    contentValues.put(OfflineTutorialContract.LessonEntry.COLUMN_IS_FAVOURITE, 0);
                    mContext.getContentResolver().update(uri,contentValues,null,null);

                    lessonsArrayList.remove(lesson);
                    notifyDataSetChanged();

                }
            });
        }



//        Log.e(TAG, lesson.getTitle()+" is here");

    }

    private void loadLessonView(int position) {
        //use LessonViewFragment to list show
        LessonViewFragment lessonViewFragment = new LessonViewFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(LessonViewFragment.ARG_LESSONS,lessonsArrayList);
        args.putInt(LessonViewFragment.ARG_LESSON_POSITION,position);
        lessonViewFragment.setArguments(args);

        if(mContext instanceof LessonActivity){
            //use lesson static variables
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.lesson_container,lessonViewFragment );
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

        }else {

            Intent favouriteViewIntent = new Intent(mContext,FavouriteViewActivity.class );
            favouriteViewIntent.putParcelableArrayListExtra(FavouriteViewActivity.EXTRA_FAVOURITES, lessonsArrayList);
            favouriteViewIntent.putExtra(FavouriteViewActivity.EXTRA_POSITION, position);
            mContext.startActivity(favouriteViewIntent);
        }



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
