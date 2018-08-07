package com.muilat.android.offlinetutorial.data;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Quiz implements Parcelable {

    private int mID;
    public String mQuestion;
    public String mAnswer;
//    public String[] mOption = new String[4];
    public String mOption2;
    public String mOption3;
    public String mOption4;
    public String mOption1;

    private int answerNr ;

    public Quiz(Cursor data) {
        mID = OfflineTutorialDbHelper.getColumnInt(data, OfflineTutorialContract.QuizEntry._ID);
        mQuestion = OfflineTutorialDbHelper.getColumnString(data, OfflineTutorialContract.QuizEntry.COLUMN_QUESTION);
        mAnswer = OfflineTutorialDbHelper.getColumnString(data, OfflineTutorialContract.QuizEntry.COLUMN_ANSWER);
        mOption1 = OfflineTutorialDbHelper.getColumnString(data, OfflineTutorialContract.QuizEntry.COLUMN_OPTION1);
        mOption2 = OfflineTutorialDbHelper.getColumnString(data, OfflineTutorialContract.QuizEntry.COLUMN_OPTION2);
        mOption3 = OfflineTutorialDbHelper.getColumnString(data, OfflineTutorialContract.QuizEntry.COLUMN_OPTION3);
        mOption4 = OfflineTutorialDbHelper.getColumnString(data, OfflineTutorialContract.QuizEntry.COLUMN_OPTION4);

        if(mOption1.equals(mAnswer)){
            answerNr = 1;
        }
        if(mOption2.equals(mAnswer)){
            answerNr = 2;
        }
        if(mOption3.equals(mAnswer)){
            answerNr = 3;
        }
        if(mOption4.equals(mAnswer)){
            answerNr = 4;
        }
    }


    protected Quiz(Parcel in) {
        mID = in.readInt();
        mQuestion = in.readString();
        mAnswer = in.readString();
        mOption2 = in.readString();
        mOption3 = in.readString();
        mOption4 = in.readString();
        mOption1 = in.readString();
        answerNr = in.readInt();
    }

    public static final Creator<Quiz> CREATOR = new Creator<Quiz>() {
        @Override
        public Quiz createFromParcel(Parcel in) {
            return new Quiz(in);
        }

        @Override
        public Quiz[] newArray(int size) {
            return new Quiz[size];
        }
    };

    public int getID() {
        return mID;
    }

    public int getAnswerNr() {
        return answerNr;
    }

    //    public String[] getOptions() {
//        return mOption;
//    }

    public String getQuestion() {
        return mQuestion;
    }

    public String getAnswer() {
        return mAnswer;
    }

    public String getOption2() {
        return mOption2;
    }

    public String getOption3() {
        return mOption3;
    }

    public String getOption4() {
        return mOption4;
    }

    public String getOption1() {
        return mOption1;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(mID);
        parcel.writeString(mQuestion);
        parcel.writeString(mAnswer);
        parcel.writeString(mOption2);
        parcel.writeString(mOption3);
        parcel.writeString(mOption4);
        parcel.writeString(mOption1);
        parcel.writeInt(answerNr);
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

