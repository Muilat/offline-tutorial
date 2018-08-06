package com.muilat.android.offlinetutorial.data;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Lessons implements Parcelable {

    private int mID;
//    private int mCategoryID;
    private String mTitle;
    private int mSubCategoryID;
    private String mDescription;
    private String mIsFavourite;

    public Lessons(Cursor data) {
        mID = OfflineTutorialDbHelper.getColumnInt(data, OfflineTutorialContract.LessonEntry._ID);
        mTitle = OfflineTutorialDbHelper.getColumnString(data, OfflineTutorialContract.LessonEntry.COLUMN_TITLE);
        mIsFavourite = OfflineTutorialDbHelper.getColumnString(data, OfflineTutorialContract.LessonEntry.COLUMN_IS_FAVOURITE);
        mDescription = OfflineTutorialDbHelper.getColumnString(data, OfflineTutorialContract.LessonEntry.COLUMN_DESCRIPTION);

    }


    public Lessons(int mID, int mCategoryID, int mSubCategoryID, String mTitle, String mDescription) {
        this.mID = mID;
//        this.mCategoryID = mCategoryID;
        this.mTitle = mTitle;
        this.mSubCategoryID = mSubCategoryID;
        this.mDescription = mDescription;
    }

    protected Lessons(Parcel in) {
        mID = in.readInt();
//        mCategoryID = in.readInt();
        mTitle = in.readString();
        mSubCategoryID = in.readInt();
        mDescription = in.readString();
    }

    public static final Creator<Lessons> CREATOR = new Creator<Lessons>() {
        @Override
        public Lessons createFromParcel(Parcel in) {
            return new Lessons(in);
        }

        @Override
        public Lessons[] newArray(int size) {
            return new Lessons[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    public int getID() {
        return mID;
    }


    public String getTitle() {
        return mTitle;
    }

    public int getSubCategoryID() {
        return mSubCategoryID;
    }

    public String getDescription() {
        return mDescription;
    }

    public boolean isFavourite(){
        if (mIsFavourite.equals("0"))
            return false;
        else
            return true;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(mID);
//        parcel.writeInt(mCategoryID);
        parcel.writeString(mTitle);
        parcel.writeInt(mSubCategoryID);
        parcel.writeString(mDescription);
    }

}

