package com.muilat.android.offlinetutorial.data;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

import static com.muilat.android.offlinetutorial.data.OfflineTutorialContract.CategoryEntry.CREATE_CATEGORIES_TABLE;

public class OfflineTutorialDbHelper extends SQLiteOpenHelper {

    private static final String TAG = OfflineTutorialDbHelper.class.getName();
    private static final String DB_NAME = "offline_tutorial.db";
    private static final int DB_VERSION = 1;


    public OfflineTutorialDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {


        Log.e(TAG, "attemmpting to create");

        db.execSQL(CREATE_CATEGORIES_TABLE);/*create words table*/

//        db.execSQL(CREATE_WORDS_TABLE);/*create words table*/

        Log.e(TAG, "Db created");

        //only isert the asset from colourValue.json if the db is being created for the first time
//        addInitWords(db);
//
//
//        addInitCategories(db);
        readCategoriesFromResources(db);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Query for altering table here
        final String ALTER_TABLE = "";

        if(ALTER_TABLE.equals("")){
            db.execSQL(ALTER_TABLE);
        }
        else
        {
            //no alter query so drop the existing database
            //drop words table
//            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

            //drop words table
            db.execSQL("DROP TABLE IF EXISTS " + OfflineTutorialContract.CategoryEntry.TABLE_NAME);


            //create a new database
            onCreate(db);

        }

    }

    public static String getColumnString(Cursor cursor, String name) {
        return cursor.getString(cursor.getColumnIndex(name));
    }

    public static int getColumnInt(Cursor cursor, String name) {
        return cursor.getInt(cursor.getColumnIndex(name));
    }

    private void readCategoriesFromResources(SQLiteDatabase db)  {

        ArrayList<Categories> categories = Categories.dummyCategories();

        for (Categories categegory: categories){
            ContentValues contentValues = new ContentValues();
            // Put the insect  into the ContentValues
            contentValues.put(OfflineTutorialContract.CategoryEntry.COLUMN_TITLE, categegory.getTitle());
            contentValues.put(OfflineTutorialContract.CategoryEntry._ID, categegory.getId());
            contentValues.put(OfflineTutorialContract.CategoryEntry.COLUMN_DESCRPTION, categegory.getDescription());
            contentValues.put(OfflineTutorialContract.CategoryEntry.COLUMN_MODIFIED_AT, "12234");

            long id =db.insert(OfflineTutorialContract.CategoryEntry.TABLE_NAME,null,contentValues);

            Log.e(TAG, "including data: category "+id);

        }
    }

}
