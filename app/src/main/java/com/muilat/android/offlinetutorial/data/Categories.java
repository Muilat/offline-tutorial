package com.muilat.android.offlinetutorial.data;


import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;

public class Categories {
    private int mID;
    private String mTitle;
    private String mDescription;
//    private byte[] mIcon;
    private String mIcon;

//

    public Categories(int mID, String mTitle, String mDescription) {
        this.mID = mID;
        this.mTitle = mTitle;
        this.mDescription = mDescription;
    }


    public Categories(Cursor data) {
        mID = OfflineTutorialDbHelper.getColumnInt(data, OfflineTutorialContract.CategoryEntry._ID);
        mTitle = OfflineTutorialDbHelper.getColumnString(data, OfflineTutorialContract.CategoryEntry.COLUMN_TITLE);
        mDescription = OfflineTutorialDbHelper.getColumnString(data, OfflineTutorialContract.CategoryEntry.COLUMN_DESCRIPTION);
        mIcon = OfflineTutorialDbHelper.getColumnString(data, OfflineTutorialContract.CategoryEntry.COLUMN_ICON);
//        mIcon = OfflineTutorialDbHelper.getColumnBlob(data, OfflineTutorialContract.CategoryEntry.COLUMN_ICON);

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

//    public byte[] getIcon() {
//        return mIcon;
//    }
    public String getIcon() {
        return mIcon;
    }


}

