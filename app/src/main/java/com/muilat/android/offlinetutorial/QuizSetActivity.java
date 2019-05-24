package com.muilat.android.offlinetutorial;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.muilat.android.offlinetutorial.adapter.QuizSetAdapter;
import com.muilat.android.offlinetutorial.data.OfflineTutorialDbHelper;

public class QuizSetActivity extends AppCompatActivity {


    QuizSetAdapter mQuizSetAdapter;

    private SQLiteDatabase mDb;
    private OfflineTutorialDbHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new OfflineTutorialDbHelper(this);
        mDb= dbHelper.getReadableDatabase();

        Cursor cursor = mDb.rawQuery("SELECT * FROM quiz_set WHERE status = 1",null);
        setContentView(R.layout.activity_quiz_set);



        RecyclerView recycler =  findViewById(R.id.quiz_set_recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recycler.setLayoutManager(layoutManager);

        mQuizSetAdapter = new QuizSetAdapter();
        recycler.setItemAnimator(new DefaultItemAnimator()); //Animator for recycler view
        recycler.setAdapter(mQuizSetAdapter);
        recycler.setHasFixedSize(true);

        mQuizSetAdapter.swapCursor(cursor);



    }


}
