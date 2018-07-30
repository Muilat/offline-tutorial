package com.muilat.android.offlinetutorial.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Words implements Parcelable {

    private int mID;
    private int mCategoryID;
    public String mTitle;
    public int mSubCategoryID;
    public String mDescription;

//    public Words(Cursor data) {
//        mID = WazoDbHelper.getColumnInt(data, WordContract.WordEntry._ID);
//        mCategoryID = WazoDbHelper.getColumnInt(data, WordContract.WordEntry.COLUMN_CATEGORY_ID);
//        mTitle = WazoDbHelper.getColumnString(data, WordContract.WordEntry.COLUMN_ENGLISH);
//        mHausa = WazoDbHelper.getColumnString(data, WordContract.WordEntry.COLUMN_HAUSA);
//        mDescription = WazoDbHelper.getColumnString(data, WordContract.WordEntry.COLUMN_YORUBA);
//
//    }


    public Words(int mID, int mCategoryID, int mSubCategoryID, String mTitle, String mDescription) {
        this.mID = mID;
        this.mCategoryID = mCategoryID;
        this.mTitle = mTitle;
        this.mSubCategoryID = mSubCategoryID;
        this.mDescription = mDescription;
    }

    protected Words(Parcel in) {
        mID = in.readInt();
        mCategoryID = in.readInt();
        mTitle = in.readString();
        mSubCategoryID = in.readInt();
        mDescription = in.readString();
    }

    public static final Creator<Words> CREATOR = new Creator<Words>() {
        @Override
        public Words createFromParcel(Parcel in) {
            return new Words(in);
        }

        @Override
        public Words[] newArray(int size) {
            return new Words[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    public int getID() {
        return mID;
    }

    public int getCategoryID() {
        return mCategoryID;
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

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(mID);
        parcel.writeInt(mCategoryID);
        parcel.writeString(mTitle);
        parcel.writeInt(mSubCategoryID);
        parcel.writeString(mDescription);
    }
    
    public static ArrayList<Words> dummyWords(){
    ArrayList<Words> words = new ArrayList<>();
        words.add(new Words(1,1, 1,"Definition of English Grammar","Lorem ipsum is simply dummy text of the printing and typesetting industry"));
        words.add(new Words(2,1, 1,"Definition of English Grammar","Lorem ipsum is simply dummy text of the printing and typesetting industry"));
        words.add(new Words(3,1, 1,"Definition of English Grammar","Lorem ipsum is simply dummy text of the printing and typesetting industry"));

        words.add(new Words(4,2, 1,"Letter","Lorem ipsum is simply dummy text of the printing and typesetting industry"));
        words.add(new Words(5,2, 1,"Letter","Lorem ipsum is simply dummy text of the printing and typesetting industry"));
        words.add(new Words(6,2, 1,"Letter","Lorem ipsum is simply dummy text of the printing and typesetting industry"));
    words.add(new Words(7,3, 1,"Number","Lorem ipsum is simply dummy text of the printing and typesetting industry"));

    return words;
}


    public static ArrayList<Words> getWordsBySubCatId(int subCatId){
        
        ArrayList<Words> words = new ArrayList<>();
        for (Words word:dummyWords()){
            if(word.getSubCategoryID() == subCatId){
                words.add(word);
            }
        }
        
        return words;
        
    }
}

