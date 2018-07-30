package com.muilat.android.offlinetutorial;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.muilat.android.offlinetutorial.data.Words;

import java.util.ArrayList;

public class DetailsActivity extends AppCompatActivity {
    public static final String EXTRA_SUB_CAT_ID = "extra_sub_cat_id";

    int sub_category_id = -1;

    ArrayList<Words> mWords = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar toolbar;

        if (getIntent() != null) {
            sub_category_id = getIntent().getIntExtra(EXTRA_SUB_CAT_ID,sub_category_id);
        }

        if (sub_category_id > 0){
            mWords = Words.getWordsBySubCatId(sub_category_id);
        }

        if (mWords.size() > 1){
            setContentView(R.layout.activity_details);
            toolbar = findViewById(R.id.toolbar);

        }else {
            toolbar = findViewById(R.id.toolbar);

        }

        setSupportActionBar(toolbar);
    }
}
