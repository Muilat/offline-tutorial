package com.muilat.android.offlinetutorial.data;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
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
import android.widget.TextView;

import com.muilat.android.offlinetutorial.R;
import com.muilat.android.offlinetutorial.util.ColorUtil;

public class SubCategoryAdapter  extends RecyclerView.Adapter<SubCategoryAdapter.ViewHolder> {

    //    private Cursor mCursor;
    Cursor mCursor;
    private final String TAG = SubCategoryAdapter.class.getSimpleName();

    Context mContext;

//    String user_lang_pref;
    static Drawable d;



    @Override
    public SubCategoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        final  ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.sub_category_item, parent, false));

//        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                int position = viewHolder.getAdapterPosition();
//                SubCategories subCategory = getItem(position);
//
//
//            }
//        });

        d = ContextCompat.getDrawable(mContext,R.drawable.first_letter_circle);


        return viewHolder;
    }

    @Override
    @TargetApi(Build.VERSION_CODES.N)
    public void onBindViewHolder(SubCategoryAdapter.ViewHolder holder, int position) {

        mCursor.moveToPosition(position); // get to the right location in the cursor

        SubCategories subCategory = new SubCategories(mCursor);

//        if(user_lang_pref.equals(mContext.getResources().getString(R.string.pref_lang_hau_yor_value))){
//            holder.name.setText(subCategory.getHausa()+"");
//
//        }else {
//            holder.name.setText(subCategory.getName()+"");
//        }
//        holder.name.setBackgroundColor(subCategory.getColorInt());
        holder.title.setText(subCategory.getTitle());
        d.setColorFilter(ColorUtil.generateColor(), PorterDuff.Mode.DARKEN);

        holder.icon.setBackground(d);
//        holder.icon.setText(subCategory.getTitle().substring(0,1));
        holder.subCategory_cardView.setTag(subCategory);

//        Log.e(TAG, subCategory.getTitle()+" is here");

    }

    @Override
    public int getItemCount() {
        if (mCursor == null) {
            return 0;
        }
        return mCursor.getCount();
    }

    /**
     * Return a {@link SubCategories} represented by this item in the adapter.
     * Method is used to run machine tests.
     *
     * @param position Cursor item position
     * @return A new {@link SubCategories}
     */
    public SubCategories getItem(int position) {
        if (mCursor.moveToPosition(position)) {
            return new SubCategories(mCursor);
        }

        return null;
    }

    /**
     * @param data update cursor
     */
    public void swapCursor(Cursor data) {
        Log.e(TAG, "Swapping SubCategories");
        mCursor = data;
        notifyDataSetChanged();
    }

    /**
     * An Recycler item view
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView title;
        public ImageView icon;
        CardView subCategory_cardView;


        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.sub_category_title);
            icon = itemView.findViewById(R.id.sub_category_icon);
            subCategory_cardView = itemView.findViewById(R.id.sub_cat_card);

        }
    }
}
