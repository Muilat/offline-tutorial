package com.muilat.android.offlinetutorial.data;

import android.database.Cursor;

public class QuizSet {
    private int mID;
    private String mName;


    public QuizSet(int mID, String mName) {
        this.mID = mID;
        this.mName = mName;
    }


    public QuizSet(Cursor data) {
        mID = OfflineTutorialDbHelper.getColumnInt(data, OfflineTutorialContract.QuizSetEntry._ID);
        mName = OfflineTutorialDbHelper.getColumnString(data, OfflineTutorialContract.QuizSetEntry.COLUMN_NAME);
    }

    public int getId() {
        return mID;
    }

    public String getName() {
        return mName;
    }


}
