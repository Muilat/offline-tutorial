package com.muilat.android.offlinetutorial.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import java.util.ArrayList;

import static com.muilat.android.offlinetutorial.data.OfflineTutorialContract.CategoryEntry.CREATE_CATEGORIES_TABLE;
import static com.muilat.android.offlinetutorial.data.OfflineTutorialContract.SubCategoryEntry.CREATE_SUB_CATEGORIES_TABLE;
import static com.muilat.android.offlinetutorial.data.OfflineTutorialContract.LessonEntry.CREATE_LESSONS_TABLE;
import static com.muilat.android.offlinetutorial.data.OfflineTutorialContract.QuizEntry.CREATE_QUIZ_TABLE;

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

        db.execSQL(CREATE_CATEGORIES_TABLE);/*create categories table*/
        db.execSQL(CREATE_SUB_CATEGORIES_TABLE);/*create subcategories table*/
        db.execSQL(CREATE_LESSONS_TABLE);/*create lessons table*/
        db.execSQL(CREATE_QUIZ_TABLE);/*create lessons table*/


        Log.e(TAG, "Db created");

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
            //drop table
            db.execSQL("DROP TABLE IF EXISTS " + OfflineTutorialContract.CategoryEntry.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + OfflineTutorialContract.SubCategoryEntry.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + OfflineTutorialContract.LessonEntry.TABLE_NAME);
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




//    private void readCategoriesFromResources(SQLiteDatabase db)  {
//
//        ArrayList<Categories> categories = Categories.dummyCategories();
//
//        for (Categories categegory: categories){
//            ContentValues contentValues = new ContentValues();
//            // Put the insect  into the ContentValues
//            contentValues.put(OfflineTutorialContract.CategoryEntry.COLUMN_TITLE, categegory.getTitle());
//            contentValues.put(OfflineTutorialContract.CategoryEntry._ID, categegory.getId());
//            contentValues.put(OfflineTutorialContract.CategoryEntry.COLUMN_DESCRIPTION, categegory.getDescription());
//            contentValues.put(OfflineTutorialContract.CategoryEntry.COLUMN_MODIFIED_AT, "12234");
//
//            long id =db.insert(OfflineTutorialContract.CategoryEntry.TABLE_NAME,null,contentValues);
//
//            Log.e(TAG, "including data: category "+id);
//
//        }
//    }

}
