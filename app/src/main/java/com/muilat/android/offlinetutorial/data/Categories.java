package com.muilat.android.offlinetutorial.data;


import android.database.Cursor;

import java.util.ArrayList;

public class Categories {
    private int mID;
    private String mTitle;
    private String mDescription;
//    private String mImageUrl;

//

    public Categories(int mID, String mTitle, String mDescription) {
        this.mID = mID;
        this.mTitle = mTitle;
        this.mDescription = mDescription;
    }


    public Categories(Cursor data) {
        mID = OfflineTutorialDbHelper.getColumnInt(data, OfflineTutorialContract.CategoryEntry._ID);
        mTitle = OfflineTutorialDbHelper.getColumnString(data, OfflineTutorialContract.CategoryEntry.COLUMN_TITLE);
        mDescription = OfflineTutorialDbHelper.getColumnString(data, OfflineTutorialContract.CategoryEntry.COLUMN_TITLE);
    }

    public int getId() {
        return mID;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getDescription() {
        return mDescription;
    }

    public static ArrayList<Categories> dummyCategories(){
        ArrayList<Categories> categories = new ArrayList<>();
        categories.add(new Categories(1,"Basic Grammar", "Lorem ipsum is simply dummy text of the printing and typesetting industry"));
        categories.add(new Categories(2,"Sentence", "Lorem ipsum is simply dummy text of the printing and typesetting industry"));
        categories.add(new Categories(3,"Part of Speach", "Lorem ipsum is simply dummy text of the printing and typesetting industry"));
        categories.add(new Categories(4,"Prepositio", "Lorem ipsum is simply dummy text of the printing and typesetting industry"));
        categories.add(new Categories(5,"Article", "Lorem ipsum is simply dummy text of the printing and typesetting industry"));
        categories.add(new Categories(6,"Tense", "Lorem ipsum is simply dummy text of the printing and typesetting industry"));

        return categories;
    }
}

