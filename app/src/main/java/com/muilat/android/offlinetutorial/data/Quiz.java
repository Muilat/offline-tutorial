package com.muilat.android.offlinetutorial.data;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Quiz  {

    private int mID;
    public String mQuestion;
    public String mAnswer;
    public String[] mOption = new String[4];
//    public String mOption2;
//    public String mOption3;
//    public String mOption4;

    public Quiz(Cursor data) {
        mID = OfflineTutorialDbHelper.getColumnInt(data, OfflineTutorialContract.QuizEntry._ID);
        mQuestion = OfflineTutorialDbHelper.getColumnString(data, OfflineTutorialContract.QuizEntry.COLUMN_QUESTION);
        mAnswer = OfflineTutorialDbHelper.getColumnString(data, OfflineTutorialContract.QuizEntry.COLUMN_ANSWER);
        mOption[0] = OfflineTutorialDbHelper.getColumnString(data, OfflineTutorialContract.QuizEntry.COLUMN_OPTION1);
        mOption[1] = OfflineTutorialDbHelper.getColumnString(data, OfflineTutorialContract.QuizEntry.COLUMN_OPTION2);
        mOption[2] = OfflineTutorialDbHelper.getColumnString(data, OfflineTutorialContract.QuizEntry.COLUMN_OPTION3);
        mOption[3] = OfflineTutorialDbHelper.getColumnString(data, OfflineTutorialContract.QuizEntry.COLUMN_OPTION4);

    }


    public int getmID() {
        return mID;
    }

    public String getQuestion() {
        return mQuestion;
    }

    public String getAnswer() {
        return mAnswer;
    }

    public String[] getOptions() {
        return mOption;
    }


//
//    public Quiz(int mID, String mQuestion, String mAnswer, String mOption1, String mOption2, String mOption3, String mOption4) {
//
//        this.mID = mID;
//        this.mQuestion = mQuestion;
//        this.mAnswer = mAnswer;
//        this.mOption1 = mOption1;
//        this.mOption2 = mOption2;
//        this.mOption3 = mOption3;
//        this.mOption4 = mOption4;
//    }
}

