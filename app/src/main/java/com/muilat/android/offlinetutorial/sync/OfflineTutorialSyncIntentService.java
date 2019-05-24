package com.muilat.android.offlinetutorial.sync;

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.util.Log;

import com.muilat.android.offlinetutorial.MainActivity;
import com.muilat.android.offlinetutorial.SplashActivity;
import com.muilat.android.offlinetutorial.data.OfflineTutorialContract;
import com.muilat.android.offlinetutorial.util.NetworkUtils;

import java.net.URL;
import java.util.ArrayList;

public class OfflineTutorialSyncIntentService extends IntentService {

    static final String TAG = OfflineTutorialSyncIntentService.class.getSimpleName();

    public OfflineTutorialSyncIntentService() {
        super("OfflineTutorialSyncIntentService");

    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Log.e(TAG, "Intent services called");
        try {
            /*
             * The getUrl method will return the URL that we need to get the forecast JSON for the
             * offlineTutorial. It will decide whether to create a URL based off of the latitude and
             * longitude or off of a simple location as a String.
             */
            URL offlineTutorialRequestUrl = NetworkUtils.getUrl(this);
//            Log.e(TAG, offlineTutorialRequestUrl.toString());

            /* Use the URL to retrieve the JSON */
            String jsonOfflineTutorialResponse = NetworkUtils.getCategoriesResponseFromHttpUrl(offlineTutorialRequestUrl);
//            Log.e(TAG, jsonOfflineTutorialResponse);

            /* Get a handle on the ContentResolver to delete and insert data */
            ContentResolver offlineTutorialContentResolver = SplashActivity.contentResolver;


            /* Parse the JSON into a list of offlineTutorial values */
            ArrayList<ContentValues[]> offlineTutorialValues = NetworkUtils.getOfflineTutorialContentValuesFromJson(this, jsonOfflineTutorialResponse);

            ContentValues[] categoriesValues = offlineTutorialValues.get(0);
            ContentValues[] subCategoriesValues = offlineTutorialValues.get(1);
            ContentValues[] lessonsValues = offlineTutorialValues.get(2);
            ContentValues[] quizValues = offlineTutorialValues.get(3);
            ContentValues[] notificationValues = offlineTutorialValues.get(4);
            ContentValues[] quizSetValues = offlineTutorialValues.get(5);
            /*
             * In cases where our JSON contained an error code, getOfflineTutorialContentValuesFromJson
             * would have returned null. We need to check for those cases here to prevent any
             * NullPointerExceptions being thrown. We also have no reason to insert fresh data if
             * there isn't any to insert.
             */
            if (categoriesValues != null && categoriesValues.length != 0) {

                /* Insert our new category data into OfflineTutorial's ContentProvider */
                offlineTutorialContentResolver.bulkInsert(
                        OfflineTutorialContract.CategoryEntry.CONTENT_URI,
                        categoriesValues);
            }


            if (subCategoriesValues != null && subCategoriesValues.length != 0) {

                /* Insert our new subcategory data into OfflineTutorial's ContentProvider */
                offlineTutorialContentResolver.bulkInsert(
                        OfflineTutorialContract.SubCategoryEntry.CONTENT_URI,
                        subCategoriesValues);
            }


            if (lessonsValues != null && lessonsValues.length != 0) {

                /* Insert our new lesson data into OfflineTutorial's ContentProvider */
                offlineTutorialContentResolver.bulkInsert(
                        OfflineTutorialContract.LessonEntry.CONTENT_URI,
                        lessonsValues);
            }

            if (quizSetValues != null && quizSetValues.length != 0) {

                /* Insert our new quiz_set data into OfflineTutorial's ContentProvider */
                offlineTutorialContentResolver.bulkInsert(
                        OfflineTutorialContract.QuizSetEntry.CONTENT_URI,
                        quizSetValues);
            }




            if (quizValues != null && quizValues.length != 0) {

                /* Insert our new lesson data into OfflineTutorial's ContentProvider */
                offlineTutorialContentResolver.bulkInsert(
                        OfflineTutorialContract.QuizEntry.CONTENT_URI,
                        quizValues);
            }

            if (notificationValues != null && notificationValues.length != 0) {

                /* Insert our new notification data into OfflineTutorial's ContentProvider */
                offlineTutorialContentResolver.bulkInsert(
                        OfflineTutorialContract.NotificationEntry.CONTENT_URI,
                        notificationValues);
            }


            /* If the code reaches this point, we have successfully performed our sync */

        } catch (Exception e) {
            /* Server probably invalid */
            Log.e(TAG, "failed ");

            e.printStackTrace();
        }
    }
}
