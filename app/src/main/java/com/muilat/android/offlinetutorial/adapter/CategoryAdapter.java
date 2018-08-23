package com.muilat.android.offlinetutorial.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.muilat.android.offlinetutorial.R;
import com.muilat.android.offlinetutorial.data.Categories;
import com.squareup.picasso.Picasso;

public class CategoryAdapter  extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

//    private Cursor mCursor;
    Cursor mCursor;
    private final String TAG = CategoryAdapter.class.getSimpleName();

    Context mContext;

    RecyclerView.RecycledViewPool viewPool;

//    String user_lang_pref;



    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        final  ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.category_item, parent, false));

        viewPool = new RecyclerView.RecycledViewPool();

//        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                int position = viewHolder.getAdapterPosition();
//                Categories category = getItem(position);
//
//
//            }
//        });

        return viewHolder;
    }

    @Override
    @TargetApi(Build.VERSION_CODES.N)
    public void onBindViewHolder(CategoryAdapter.ViewHolder holder, int position) {

        mCursor.moveToPosition(position); // get to the right location in the cursor

        Categories category = new Categories(mCursor);

        holder.title.setText(category.getTitle());
//        holder.description.setText(category.getDescription());
//        byte[] image = category.getIcon();
        String image = category.getIcon();

        if(image != null && !image.equals("")){
            Picasso.get().load(image)
                    .placeholder(R.drawable.books)
                    .into(holder.icon);

//            Log.e(TAG, image);
//            Bitmap bmp = BitmapFactory.decodeByteArray(image, 0, image.length);
//
//            holder.icon.setImageBitmap(Bitmap.createScaledBitmap(bmp, 100,
//                    100, false));
        }


        holder.category_cardView.setTag(category);
//        holder.itemView.setTag(category);
//        holder.category_cardView.setBackgroundColor(ColorUtil.generateColor());


//        holder.sub_cat_recyclerView.setRecycledViewPool(viewPool);

    }

    @Override
    public int getItemCount() {
        if (mCursor == null) {
            return 0;
        }
        return mCursor.getCount();
    }

    /**
     * Return a {@link Categories} represented by this item in the adapter.
     * Method is used to run machine tests.
     *
     * @param position Cursor item position
     * @return A new {@link Categories}
     */
    public Categories getItem(int position) {
        if (mCursor.moveToPosition(position)) {
            return new Categories(mCursor);
        }

        return null;
    }

    /**
     * @param data update cursor
     */
    public void swapCursor(Cursor data) {
        Log.e(TAG, "Swapping Categories");
        mCursor = data;
        notifyDataSetChanged();
    }

    /**
     * An Recycler item view
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView title, description;
        CardView category_cardView;
        ImageView icon;
//        RecyclerView sub_cat_recyclerView;


        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.category_title);
//            description = itemView.findViewById(R.id.category_description);
            icon = itemView.findViewById(R.id.category_icon);
            category_cardView = itemView.findViewById(R.id.category_card);

//            sub_cat_recyclerView = itemView.findViewById(R.id.sub_cat_recyclerView);

        }
    }
}
