package com.muilat.android.offlinetutorial.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.muilat.android.offlinetutorial.NotificationActivity;
import com.muilat.android.offlinetutorial.R;
import com.muilat.android.offlinetutorial.data.Notifications;
import com.squareup.picasso.Picasso;

public class NotificationAdapter  extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    //    private Cursor mCursor;
    Cursor mCursor;
    private final String TAG = NotificationAdapter.class.getSimpleName();

    Context mContext;


    @Override
    public NotificationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        final  ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.notification_item, parent, false));

        return viewHolder;
    }

    @Override
    @TargetApi(Build.VERSION_CODES.N)
    public void onBindViewHolder(NotificationAdapter.ViewHolder holder, int position) {

        mCursor.moveToPosition(position); // get to the right location in the cursor

        Notifications notification = new Notifications(mCursor);

        holder.title.setText(notification.getTitle());
        holder.message.setText(notification.getMessage());
//        holder.description.setText(notification.getDescription());
        String image = notification.getImage();

        if(!image.equals("null") && !image.equals("") && image!=null) {

            holder.image.setVisibility(View.VISIBLE);
            Picasso.get().load(image)
//                    .placeholder(R.drawable.books)
                    .into(holder.image);
        }

        if (!notification.getLink().equals("")){
            holder.external_link_layout.setVisibility(View.VISIBLE);

            final String link = notification.getLink();

            SpannableString content = new SpannableString(link);
            content.setSpan(new UnderlineSpan(), 0, link
                    .length(), 0);
            content.setSpan(new ClickableSpan() {
                @Override
                public void onClick(View view) {
                    mContext.startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse(link.toLowerCase())));}
            }, 0, link.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.link.setText(content);
            holder.link.setMovementMethod(LinkMovementMethod.getInstance());
        }

        holder.itemView.setTag(notification);

    }

    @Override
    public int getItemCount() {
        if (mCursor == null) {
            return 0;
        }
        return mCursor.getCount();
    }

    /**
     * Return a {@link Notifications} represented by this item in the adapter.
     * Method is used to run machine tests.
     *
     * @param position Cursor item position
     * @return A new {@link Notifications}
     */
    public Notifications getItem(int position) {
        if (mCursor.moveToPosition(position)) {
            return new Notifications(mCursor);
        }

        return null;
    }

    /**
     * @param data update cursor
     */
    public void swapCursor(Cursor data) {
        Log.e(TAG, "Swapping Notifications");
        mCursor = data;
        notifyDataSetChanged();
    }

    /**
     * An Recycler item view
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView title, message, link;
        LinearLayout external_link_layout;
        ImageView image;
//        RecyclerView sub_cat_recyclerView;


        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.notification_title);
            message = itemView.findViewById(R.id.notification_message);
            link = itemView.findViewById(R.id.notification_external_link);
            image = itemView.findViewById(R.id.notification_image);
            external_link_layout = itemView.findViewById(R.id.external_link_layout);

//            sub_cat_recyclerView = itemView.findViewById(R.id.sub_cat_recyclerView);

        }
    }
}
