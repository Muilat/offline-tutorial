package com.muilat.android.offlinetutorial.data;


import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class SubCategories implements Parcelable {
    private int mID;
    private int mCategoryID;
    private String mTitle;
//    private String mImageUrl;

//

    public SubCategories(int mID,int mCategoryID, String mTitle) {
        this.mID = mID;
        this.mID = mCategoryID;
        this.mTitle = mTitle;
    }


//    public SubCategories(Cursor data) {
//        mID = WazoDbHelper.getColumnInt(data, CategoryContract.CategoryEntry._ID);
//        mName = WazoDbHelper.getColumnString(data, CategoryContract.CategoryEntry.COLUMN_NAME);
//        mHausa = WazoDbHelper.getColumnString(data, CategoryContract.CategoryEntry.COLUMN_HAUSA);
//        mImageUrl = WazoDbHelper.getColumnString(data, CategoryContract.CategoryEntry.COLUMN_IMAGE_URL);
//    }

    protected SubCategories(Parcel in) {
        mID = in.readInt();
        mCategoryID = in.readInt();
        mTitle = in.readString();
    }

    public static final Creator<SubCategories> CREATOR = new Creator<SubCategories>() {
        @Override
        public SubCategories createFromParcel(Parcel in) {
            return new SubCategories(in);
        }

        @Override
        public SubCategories[] newArray(int size) {
            return new SubCategories[size];
        }
    };

    public int getId() {
        return mID;
    }

    public int getCategoryId() {
        return mID;
    }

    public String getTitle() {
        return mTitle;
    }

    public static ArrayList<SubCategories> dummySubCategories(){
        ArrayList<SubCategories> subCategories = new ArrayList<>();
        subCategories.add(new SubCategories(1, 1,"Definition"));
        subCategories.add(new SubCategories(2, 1,"Letter and word"));
        subCategories.add(new SubCategories(3, 1,"Number"));
        subCategories.add(new SubCategories(5, 1,"Gender"));
        subCategories.add(new SubCategories(6, 1,"Person"));

        subCategories.add(new SubCategories(7, 2,"Defiinition"));
        subCategories.add(new SubCategories(8, 2,"Assertive Sentence"));
        subCategories.add(new SubCategories(9, 2,"Interrogative Sentence"));
        subCategories.add(new SubCategories(10, 2,"Imperative Sentence"));

        subCategories.add(new SubCategories(11,3,"Definition"));
        subCategories.add(new SubCategories(12,3,"Noun"));
        subCategories.add(new SubCategories(13,3,"Pronoun"));
        subCategories.add(new SubCategories(14,3,"Adjective"));

        subCategories.add(new SubCategories(15,4,"Definition"));
        subCategories.add(new SubCategories(16,4,"At"));
        subCategories.add(new SubCategories(17,4,"About"));

        subCategories.add(new SubCategories(18,5,"Definition"));
        subCategories.add(new SubCategories(19,5,"A/AN "));

        subCategories.add(new SubCategories(20,6,"Definition"));

        return subCategories;
    }

    public static ArrayList<SubCategories> getSubCatByCatId(int category_id){
        ArrayList<SubCategories> subCategories = new ArrayList<>();
        for (SubCategories subcat:dummySubCategories()) {
            if(subcat.getCategoryId() == category_id)
                subCategories.add(subcat);
        }

        return subCategories;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(mID);
        parcel.writeInt(mCategoryID);
        parcel.writeString(mTitle);
    }
}

