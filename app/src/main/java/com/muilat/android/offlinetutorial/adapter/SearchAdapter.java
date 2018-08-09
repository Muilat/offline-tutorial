package com.muilat.android.offlinetutorial.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.muilat.android.offlinetutorial.LessonViewFragment;
import com.muilat.android.offlinetutorial.R;
import com.muilat.android.offlinetutorial.SearchActivity;
import com.muilat.android.offlinetutorial.data.Lessons;
import com.muilat.android.offlinetutorial.util.ColorUtil;

import java.util.ArrayList;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> implements Filterable {
    private ArrayList<Lessons> searchList;
    private ArrayList<Lessons> searchListFull;
    private Context mContext;
    static Drawable d;


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

    public SearchAdapter(Context context, ArrayList<Lessons> searchList) {
        this.searchList = searchList;
        searchListFull = searchList;
        this.mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.lesson_item,
                parent, false));

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int position = viewHolder.getAdapterPosition();
//                Lessons lesson = getItem(position);
                Intent lessonViewIntent = new Intent(mContext, LessonViewFragment.class);
                lessonViewIntent.putParcelableArrayListExtra(LessonViewFragment.ARG_LESSONS,searchList);
                lessonViewIntent.putExtra(LessonViewFragment.ARG_LESSON_POSITION,position);
                mContext.startActivity(lessonViewIntent);

            }
        });
        d = ContextCompat.getDrawable(mContext,R.drawable.first_letter_circle);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Lessons lesson = searchList.get(position);

        holder.title.setText(lesson.getTitle());
        holder.icon.setText(position+1+"");

        d.setColorFilter(ColorUtil.generateColor(), PorterDuff.Mode.DARKEN);

        if(lesson.getDescription().length() >50){
            holder.description.setText(lesson.getDescription().substring(0,47)+"...");

        }
        holder.word_cardView.setTag(lesson.getID());

    }

    @Override
    public int getItemCount() {
        return searchList.size();
    }

    /**
     * @param data update cursor
     */
    public void swapCursor(ArrayList<Lessons> data) {
        searchList = searchListFull = data;
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<Lessons> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(searchListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Lessons lesson : searchListFull) {
                    if (lesson.getDescription().toLowerCase().contains(filterPattern) ||
                            lesson.getTitle().toLowerCase().contains(filterPattern)) {
                        filteredList.add(lesson);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            searchList.clear();
            searchList.addAll((ArrayList) results.values);
            notifyDataSetChanged();
        }
    };
}
