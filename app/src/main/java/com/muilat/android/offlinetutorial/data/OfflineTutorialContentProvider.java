package com.muilat.android.offlinetutorial.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import static com.muilat.android.offlinetutorial.data.OfflineTutorialContract.*;

public class OfflineTutorialContentProvider extends ContentProvider {

    public static final int CATEGORIES = 110;
    public static final int SUB_CATEGORIES = 120;
    public static final int LESSONS = 130;
    public static final int QUIZ = 140;
    public static final int FAVOURITES = 150;
    public static final int NOTIFICATIONS = 160;
    public static final int QUIZ_SET = 170;

    public static final int CATEGORY_WITH_ID = 101;
    public static final int SUB_CATEGORY_WITH_ID = 102;
    public static final int LESSON_WITH_ID = 103;
//    public static final int QUIZ_WITH_ID = 104;
    public static final int NOTIFICATION_WITH_ID = 106;
    public static final int QUIZ_WITH_QUIZ_SET_ID = 107;//qui


    // Declare a static variable for the Uri matcher that you construct
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private static final String TAG = OfflineTutorialContentProvider.class.getSimpleName();

    // Define a static buildUriMatcher method that associates URI's with their int match
    /**
     Initialize a new matcher object without any matches,
     then use .addURI(String authority, String path, int match) to add matches
     */
    public static UriMatcher buildUriMatcher() {

        // Initialize a UriMatcher with no matches by passing in NO_MATCH to the constructor
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    /*
      All paths added to the UriMatcher have a corresponding int.
      For each kind of uri you may want to access, add the corresponding match with addURI.
      The two calls below add matches for the task directory and a single item by ID.
     */
        uriMatcher.addURI(OfflineTutorialContract.CONTENT_AUTHORITY, OfflineTutorialContract.PATH_CATEGORIES, CATEGORIES);
        uriMatcher.addURI(OfflineTutorialContract.CONTENT_AUTHORITY, OfflineTutorialContract.PATH_CATEGORIES + "/#", CATEGORY_WITH_ID);
        uriMatcher.addURI(OfflineTutorialContract.CONTENT_AUTHORITY, OfflineTutorialContract.PATH_SUB_CATEGORIES, SUB_CATEGORIES);
        uriMatcher.addURI(OfflineTutorialContract.CONTENT_AUTHORITY, OfflineTutorialContract.PATH_SUB_CATEGORIES + "/#", SUB_CATEGORY_WITH_ID);
        uriMatcher.addURI(OfflineTutorialContract.CONTENT_AUTHORITY, OfflineTutorialContract.PATH_LESSONS, LESSONS);
        uriMatcher.addURI(OfflineTutorialContract.CONTENT_AUTHORITY, OfflineTutorialContract.PATH_FAVOURITES, FAVOURITES);
        uriMatcher.addURI(OfflineTutorialContract.CONTENT_AUTHORITY, OfflineTutorialContract.PATH_LESSONS + "/#", LESSON_WITH_ID);
        uriMatcher.addURI(OfflineTutorialContract.CONTENT_AUTHORITY, OfflineTutorialContract.PATH_QUIZ, QUIZ);
//        uriMatcher.addURI(OfflineTutorialContract.CONTENT_AUTHORITY, OfflineTutorialContract.PATH_QUIZ + "/#", QUIZ_WITH_ID);
        uriMatcher.addURI(OfflineTutorialContract.CONTENT_AUTHORITY, OfflineTutorialContract.PATH_NOTIFICATIONS, NOTIFICATIONS);
        uriMatcher.addURI(OfflineTutorialContract.CONTENT_AUTHORITY, OfflineTutorialContract.PATH_NOTIFICATIONS + "/#", NOTIFICATION_WITH_ID);
        uriMatcher.addURI(OfflineTutorialContract.CONTENT_AUTHORITY, OfflineTutorialContract.PATH_QUIZ_SET, QUIZ_SET);
        uriMatcher.addURI(OfflineTutorialContract.CONTENT_AUTHORITY, OfflineTutorialContract.PATH_QUIZ + "/#", QUIZ_WITH_QUIZ_SET_ID);

        return uriMatcher;
    }

    // Member variable for a OfflineTutorialDbHelper that's initialized in the onCreate() method
    private OfflineTutorialDbHelper mOfflineTutorialDbHelper;


    @Override
    public boolean onCreate() {

        Context context = getContext();
        mOfflineTutorialDbHelper = new OfflineTutorialDbHelper(context);
        return true;
    }


    // Implement insert to handle requests to insert a single new row of data
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        // Get access to the task database (to write new data to)
        final SQLiteDatabase db = mOfflineTutorialDbHelper.getWritableDatabase();

        // Write URI matching code to identify the match for the categories directory
        int match = sUriMatcher.match(uri);
        Uri returnUri; // URI to be returned
        long id;
        switch (match) {
            case CATEGORIES:
                // Insert new values into the database
                // Inserting values into categories table
                returnUri = insertSingleValue(uri, db, values, CategoryEntry.TABLE_NAME);
                break;
            case SUB_CATEGORIES:
                // Insert new values into the database
                // Inserting values into categories table
                returnUri = insertSingleValue(uri, db, values, SubCategoryEntry.TABLE_NAME);

                break;

            case LESSONS:
                // Insert new values into the database
                // Inserting values into categories table
                returnUri = insertSingleValue(uri, db, values, LessonEntry.TABLE_NAME);

                break;
            case QUIZ:
                // Insert new values into the database
                // Inserting values into quiz table
                returnUri = insertSingleValue(uri, db, values, QuizEntry.TABLE_NAME);
                break;
            case QUIZ_SET:
                // Insert new values into the database
                // Inserting values into quiz_set table
                returnUri = insertSingleValue(uri, db, values, QuizSetEntry.TABLE_NAME);
                break;
            case NOTIFICATIONS:
                // Insert new values into the database
                // Inserting values into notification table
                returnUri = insertSingleValue(uri, db, values, NotificationEntry.TABLE_NAME);
                break;
//            // Set the value for the returnedUri and write the default case for unknown URI's
            // Default case throws an UnsupportedOperationException
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Notify the resolver if the uri has been changed, and return the newly inserted URI
        getContext().getContentResolver().notifyChange(uri, null);

        // Return constructed uri (this points to the newly inserted row of data)
        return returnUri;
    }


    // Implement query to handle requests for data by URI
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        // Get access to underlying database (read-only for query)
        final SQLiteDatabase db = mOfflineTutorialDbHelper.getReadableDatabase();

        // Write URI match code and set a variable to return a Cursor
        int match = sUriMatcher.match(uri);
        Cursor retCursor;
        String id;

        // Query for the categories directory and a default case
        switch (match) {

            // Query for the categories directory
            case CATEGORIES:
                retCursor =  db.query(CategoryEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            // Handle the single category case, recognized by the ID included in the URI path
            case CATEGORY_WITH_ID:
                // Get the task ID from the URI path
                id = uri.getPathSegments().get(1);
                // Use selections/selectionArgs to filter for this ID

                retCursor =  db.query(CategoryEntry.TABLE_NAME,
                        projection,
                        "_id=?",
                        new String[]{id},
                        null,
                        null,
                        sortOrder);
                break;
            // Query for the sub categories directory
            case SUB_CATEGORIES:
                retCursor =  db.query(SubCategoryEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            // Handle the single sub category case, recognized by the ID included in the URI path
            case SUB_CATEGORY_WITH_ID:
                // Get the task ID from the URI path
                id = uri.getPathSegments().get(1);
                // Use selections/selectionArgs to filter for this ID

                retCursor =  db.query(SubCategoryEntry.TABLE_NAME,
                        projection,
                        "category_id=?",//searh with category_id not subcate id
                        new String[]{id},
                        null,
                        null,
                        sortOrder);
                break;

            // Query for the lesson directory
            case LESSONS:
                retCursor =  db.query(LessonEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            // Handle the single category case, recognized by the ID included in the URI path
            case LESSON_WITH_ID: //lesson with sub_category_id
                // Get the task ID from the URI path
                id = uri.getPathSegments().get(1);
                // Use selections/selectionArgs to filter for this ID

                retCursor =  db.query(LessonEntry.TABLE_NAME,
                        projection,
                        "sub_category_id=?",//searh with csub_ategory_id not lesson id
                        new String[]{id},
                        null,
                        null,
                        sortOrder);
                break;

            // Handle the favourites case, recognized by the ID included in the URI path
            case FAVOURITES:

                retCursor =  db.query(LessonEntry.TABLE_NAME,
                        projection,
                        "is_favourite=?",
                        new String[]{"1"},
                        null,
                        null,
                        sortOrder);
                break;
            // Query for the quiz_set directory
            case QUIZ_SET:
                retCursor =  db.query(QuizSetEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case QUIZ_WITH_QUIZ_SET_ID: //quiz with quiz_set_id
                // Get the quiz_set ID from the URI path
                id = uri.getPathSegments().get(1);
                // Use selections/selectionArgs to filter for this ID

                retCursor =  db.query(QuizEntry.TABLE_NAME,
                        projection,
                        "quiz_set_id=?",//search with quiz_set_id not quiz id
                        new String[]{id},
                        null,
                        null,
                        sortOrder);
                break;
            // Query for the quiz directory
//            case QUIZ:
//                retCursor =  db.query(QuizEntry.TABLE_NAME,
//                        projection,
//                        selection,
//                        selectionArgs,
//                        null,
//                        null,
//                        sortOrder);
//                break;
            // Handle the single quiz case, recognized by the ID included in the URI path
//            case QUIZ_WITH_ID: //quiz with quiz_id
//                // Get the task ID from the URI path
//                id = uri.getPathSegments().get(1);
//                // Use selections/selectionArgs to filter for this ID
//
//                retCursor =  db.query(QuizEntry.TABLE_NAME,
//                        projection,
//                        "_id=?",
//                        new String[]{id},
//                        null,
//                        null,
//                        sortOrder);
//                break;
            // Query for the notitications directory
            case NOTIFICATIONS:
                retCursor =  db.query(NotificationEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            // Handle the single notification case, recognized by the ID included in the URI path
            case NOTIFICATION_WITH_ID: //lesson with sub_category_id
                // Get the task ID from the URI path
                id = uri.getPathSegments().get(1);
                // Use selections/selectionArgs to filter for this ID

                retCursor =  db.query(NotificationEntry.TABLE_NAME,
                        projection,
                        "_id=?",//searh with csub_ategory_id not lesson id
                        new String[]{id},
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Set a notification URI on the Cursor and return that Cursor
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);

        // Return the desired Cursor
        return retCursor;
    }


    // Implement delete to delete a single row of data
    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {

        // Get access to the database and write URI matching code to recognize a single item
        final SQLiteDatabase db = mOfflineTutorialDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        // Keep track of the number of deleted categories
        int recordsDeleted; // starts as 0
        String id;
        // Write the code to delete a single row of data
        // [Hint] Use selections to delete an item by its row ID
        switch (match) {
            // Handle the single item case, recognized by the ID included in the URI path
            case CATEGORY_WITH_ID:
                // Get the task ID from the URI path
                 id= uri.getPathSegments().get(1);
                // Use selections/selectionArgs to filter for this ID
                recordsDeleted = db.delete(CategoryEntry.TABLE_NAME, "_id=?", new String[]{id});
                break;
            case SUB_CATEGORY_WITH_ID:
                // Get the task ID from the URI path
                id = uri.getPathSegments().get(1);
                // Use selections/selectionArgs to filter for this ID
                recordsDeleted = db.delete(SubCategoryEntry.TABLE_NAME, "_id=?", new String[]{id});
                break;
            case LESSON_WITH_ID:
                // Get the task ID from the URI path
                id = uri.getPathSegments().get(1);
                // Use selections/selectionArgs to filter for this ID
                recordsDeleted = db.delete(LessonEntry.TABLE_NAME, "_id=?", new String[]{id});
                break;
//            case QUIZ_WITH_ID:
//                // Get the task ID from the URI path
//                id = uri.getPathSegments().get(1);
//                // Use selections/selectionArgs to filter for this ID
//                recordsDeleted = db.delete(QuizEntry.TABLE_NAME, "_id=?", new String[]{id});
//                break;
//            case QUIZ_SET_WITH_ID:
//                // Get the task ID from the URI path
//                id = uri.getPathSegments().get(1);
//                // Use selections/selectionArgs to filter for this ID
//                recordsDeleted = db.delete(QuizSetEntry.TABLE_NAME, "_id=?", new String[]{id});
//                break;
            case NOTIFICATION_WITH_ID:
                // Get the task ID from the URI path
                id = uri.getPathSegments().get(1);
                // Use selections/selectionArgs to filter for this ID
                recordsDeleted = db.delete(NotificationEntry.TABLE_NAME, "_id=?", new String[]{id});
                break;
            case NOTIFICATIONS:
                recordsDeleted = db.delete(NotificationEntry.TABLE_NAME, null, null);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Notify the resolver of a change and return the number of items deleted
        if (recordsDeleted != 0) {
            // A task was deleted, set notification
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Return the number of categories deleted
        return recordsDeleted;
    }


    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {

        // Get access to the database and write URI matching code to recognize a single item
        final SQLiteDatabase db = mOfflineTutorialDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        // Keep track of the number of updated category
        int itemUpdated; // starts as 0

        String id;
        // [Hint] Use selections to update an item by its row ID
        switch (match) {
            // Handle the single item case, recognized by the ID included in the URI path
            case CATEGORY_WITH_ID:
                // Get the category ID from the URI path
                id = uri.getPathSegments().get(1);
                // Use selections/selectionArgs to filter for this ID

                itemUpdated = db.update(CategoryEntry.TABLE_NAME,values, "_id=?", new String[]{id});
                break;
            // Handle the single item case, recognized by the ID included in the URI path
            case LESSON_WITH_ID:
                // Get the category ID from the URI path
                id = uri.getPathSegments().get(1);
                // Use selections/selectionArgs to filter for this ID

                itemUpdated = db.update(LessonEntry.TABLE_NAME,values, "_id=?", new String[]{id});
                break;
            case NOTIFICATION_WITH_ID:
                // Get the category ID from the URI path
                id = uri.getPathSegments().get(1);
                // Use selections/selectionArgs to filter for this ID

                itemUpdated = db.update(NotificationEntry.TABLE_NAME,values, "_id=?", new String[]{id});
                break;
            case NOTIFICATIONS:

                itemUpdated = db.update(NotificationEntry.TABLE_NAME,values, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Notify the resolver of a change and return the number of items deleted
        if (itemUpdated != 0) {
            // An item was updated, set notification
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Return the number of tasks deleted
        return itemUpdated;
    }


    @Override
    public String getType(@NonNull Uri uri) {

        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * @param uri    The content:// URI of the insertion request.
     * @param values An array of sets of column_name/value pairs to add to the database.
     *               This must not be {@code null}.
     *
     * @return The number of values that were inserted.
     */
    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        final SQLiteDatabase db = mOfflineTutorialDbHelper.getWritableDatabase();

        switch (sUriMatcher.match(uri)) {

            case CATEGORIES:
//                int insertedRows =
                return insertValues(uri, values, db, CategoryEntry.TABLE_NAME);


            case SUB_CATEGORIES:
                return insertValues(uri, values, db, SubCategoryEntry.TABLE_NAME);

            case LESSONS:
                return insertValues(uri, values, db, LessonEntry.TABLE_NAME);
            case QUIZ_SET:
                return insertValues(uri, values, db, QuizSetEntry.TABLE_NAME);
            case QUIZ:
                return insertValues(uri, values, db, QuizEntry.TABLE_NAME);

            case NOTIFICATIONS:
                return insertValues(uri, values, db, NotificationEntry.TABLE_NAME);

            default:
                return super.bulkInsert(uri, values);
        }
    }

    private int insertValues(@NonNull Uri uri, @NonNull ContentValues[] values, SQLiteDatabase db, String table_name) {
        int rowsInserted;
        rowsInserted = 0;
        db.beginTransaction();
        try {
            for (ContentValues value : values) {

                long _id =-1;

//                Log.e(TAG,value.getAsString("_id"));

                Cursor data = db.query(table_name,
                        null,
                        "_id=?",//searh with csub_ategory_id not lesson id
                        new String[]{value.getAsString("_id")},
                        null,
                        null,
                        null);
                if(data.getCount()!=0){
                    if(value.getAsInteger("status")==0){
                        db.delete(table_name,"_id=?",
                                new String[]{value.getAsString("_id")});
                    }else {
                        db.update(table_name,value,"_id=?",new String[]{value.getAsString("_id")});
                    }
//                    if(value.getAsString("_id"))

                }else{
//                    Log.e(TAG, "we are here");
                    _id = db.insert(table_name, null, value);
                }

                if (_id != -1) {
                    rowsInserted++;
                    getContext().getContentResolver().notifyChange(uri, null);

                }
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }

        if (rowsInserted > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        Log.e(TAG,rowsInserted+" "+table_name+" inserted");
        return rowsInserted;
    }

    private Uri insertSingleValue(Uri uri, SQLiteDatabase db, ContentValues values, String table_name){
        long id;
        Uri returnUri;
        id = db.insert(CategoryEntry.TABLE_NAME, null, values);
        if ( id > 0 ) {
            returnUri = ContentUris.withAppendedId(OfflineTutorialContract.CategoryEntry.CONTENT_URI, id);
        } else {
            throw new android.database.SQLException("Failed to insert row into " + uri);
        }

        return returnUri;
    }
}
