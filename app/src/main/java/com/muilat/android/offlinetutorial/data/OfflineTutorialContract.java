package com.muilat.android.offlinetutorial.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class OfflineTutorialContract  {
    private OfflineTutorialContract() {}

    /**
     * The "Content authority" is a name for the entire content provider, similar to the
     * relationship between a domain name and its website.  A convenient string to use for the
     * content authority is the package name for the app, which is guaranteed to be unique on the
     * device.
     */
    public static final String CONTENT_AUTHORITY = "com.muilat.android.offlinetutorial";

    /**
     * Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
     * the content provider.
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);


    public static final String PATH_CATEGORIES = "categories";

    public static final String PATH_SUB_CATEGORIES= "sub_categories";

    public static final String PATH_LESSONS= "lessons";

    public final static String PATH_QUIZ = "quiz";
    public final static String PATH_FAVOURITES = "favourites";

    //categories table
    public static final class CategoryEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_CATEGORIES).build();

        public static final String TABLE_NAME = "categories";

        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_MODIFIED_AT = "modified_at";
//        public static final String COLUMN_STATUS = "status";

        final static String CREATE_CATEGORIES_TABLE = "CREATE TABLE "  + TABLE_NAME + " (" +
                _ID+ " INTEGER  NOT NULL, " +
                COLUMN_TITLE + " TEXT NOT NULL,"+
                COLUMN_DESCRIPTION + " TEXT NOT NULL, "+
//                COLUMN_STATUS + " INTEGER NULL, "+
                COLUMN_MODIFIED_AT + " INT NOT NULL);";
    }

    //subcategories table
    public static final class SubCategoryEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_SUB_CATEGORIES).build();

        public static final String TABLE_NAME = "sub_categories";

        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_CATEGORY_ID = "category_id";
        public static final String COLUMN_MODIFIED_AT = "modified_at";
//        public static final String COLUMN_STATUS = "status";

        final static String CREATE_SUB_CATEGORIES_TABLE = "CREATE TABLE "  + TABLE_NAME + " (" +
                _ID+ " INTEGER  NOT NULL, " +
                COLUMN_TITLE + " TEXT NOT NULL,"+
                COLUMN_CATEGORY_ID + " INTEGER NOT NULL, "+
//                COLUMN_STATUS + " INTEGER NULL, "+
                COLUMN_MODIFIED_AT + " INT NOT NULL);";
    }

    //quiz table
    public static final class LessonEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_LESSONS).build();

        public static final Uri FAVOURITE_CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVOURITES).build();


        public static final String TABLE_NAME = "lessons";

        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_SUB_CATEGORY_ID = "sub_category_id";
        public static final String COLUMN_IS_FAVOURITE = "is_favourite";
        public static final String COLUMN_MODIFIED_AT = "modified_at";
//        public static final String COLUMN_STATUS = "status";

        final static String CREATE_LESSONS_TABLE = "CREATE TABLE "  + TABLE_NAME + " (" +
                _ID+ " INTEGER  NOT NULL, " +
                COLUMN_SUB_CATEGORY_ID+ " INTEGER  NOT NULL, " +
                COLUMN_TITLE + " TEXT NOT NULL,"+
                COLUMN_DESCRIPTION + " TEXT NOT NULL, "+
                COLUMN_IS_FAVOURITE + " INTEGER NULL DEFAULT 0, "+
                COLUMN_MODIFIED_AT + " INT NOT NULL);";
    }


    //quiz table
    public static final class QuizEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_QUIZ).build();

        public static final String TABLE_NAME = "quiz";

        public static final String COLUMN_QUESTION = "question";
        public static final String COLUMN_ANSWER = "answer";
        public static final String COLUMN_MODIFIED_AT = "modified_at";
        public static final String COLUMN_STATUS = "status";
        public static final String COLUMN_OPTION1 = "option1";
        public static final String COLUMN_OPTION2 = "option2";
        public static final String COLUMN_OPTION3 = "option3";
        public static final String COLUMN_OPTION4 = "option4";

        final static String CREATE_QUIZ_TABLE = "CREATE TABLE "  + TABLE_NAME + " (" +
                _ID+ " INTEGER  NOT NULL, " +
                COLUMN_QUESTION + " TEXT NOT NULL,"+
                COLUMN_ANSWER + " TEXT NOT NULL,"+
                COLUMN_OPTION1 + " TEXT NOT NULL, "+
                COLUMN_OPTION2 + " TEXT NOT NULL, "+
                COLUMN_OPTION3 + " TEXT NOT NULL, "+
                COLUMN_OPTION4 + " TEXT NOT NULL, "+
                COLUMN_STATUS + " INTEGER NULL, "+
                COLUMN_MODIFIED_AT + " INT NOT NULL);";
    }

}
