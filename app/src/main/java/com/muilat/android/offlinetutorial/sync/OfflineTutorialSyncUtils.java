package com.muilat.android.offlinetutorial.sync;

import android.content.Context;
        import android.content.Intent;
        import android.database.Cursor;
        import android.net.Uri;
        import android.os.AsyncTask;
        import android.support.annotation.NonNull;

import com.muilat.android.offlinetutorial.data.OfflineTutorialContract;


public class OfflineTutorialSyncUtils {

    //  COMPLETED (1) Declare a private static boolean field called sInitialized
    private static boolean sInitialized;

    /**
     * Creates periodic sync tasks and checks to see if an immediate sync is required. If an
     * immediate sync is required, this method will take care of making sure that sync occurs.
     *
     * @param context Context that will be passed to other methods and used to access the
     *                ContentResolver
     */
    //  COMPLETED (2) Create a synchronized public static void method called initialize
    synchronized public static void initialize(@NonNull final Context context) {

        /*
         * Only perform initialization once per app lifetime. If initialization has already been
         * performed, we have nothing to do in this method.
         */
//        if (sInitialized) return;
//
//        sInitialized = true;

        /*
         * We need to check to see if our ContentProvider has data to display in our forecast
         * list. However, performing a query on the main thread is a bad idea as this may
         * cause our UI to lag. Therefore, we create a thread in which we will run the query
         * to check the contents of our ContentProvider.
         */
        new AsyncTask<Void, Void, Void>() {
            @Override
            public Void doInBackground( Void... voids ) {

                /* URI for every row of weather data in our weather table*/
                Uri categoryUri = OfflineTutorialContract.CategoryEntry.CONTENT_URI;

                /* Here, we perform the query to check to see if we have any Offline data */
                Cursor cursor = context.getContentResolver().query(
                        categoryUri,
                        null,
                        null,
                        null,
                        null);
                /*
                 * A Cursor object can be null for various different reasons. A few are
                 * listed below.
                 *
                 *   1) Invalid URI
                 *   2) A certain ContentProvider's query method returns null
                 *   3) A RemoteException was thrown.
                 *
                 * Bottom line, it is generally a good idea to check if a Cursor returned
                 * from a ContentResolver is null.
                 *
                 * If the Cursor was null OR if it was empty, we need to sync immediately to
                 * be able to display data to the user.
                 */
                if (null == cursor || cursor.getCount() == 0) {
                    startImmediateSync(context);
                }

                /* Make sure to close the Cursor to avoid memory leaks! */
                cursor.close();
                return null;
            }
        }.execute();
    }

    /**
     * Helper method to perform a sync immediately using an IntentService for asynchronous
     * execution.
     *
     * @param context The Context used to start the IntentService for the sync.
     */
    public static void startImmediateSync(@NonNull final Context context) {
        Intent intentToSyncImmediately = new Intent(context, OfflineTutorialSyncIntentService.class);
        context.startService(intentToSyncImmediately);
    }
}
