package com.muilat.android.offlinetutorial.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.muilat.android.offlinetutorial.data.OfflineTutorialContract;
import com.muilat.android.offlinetutorial.pref.OfflineTutorialPreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class NetworkUtils {

    private static final String OFFLINE_TUTORIAL_APP_URL =
            "https://muilat-champ.000webhostapp.com/api.php";

    private static final String INITIAL_QUERY = "init_data";
    private static final String UPDATE_QUERY = "new_data";
    private static final String LAST_CHECK_TIME = "last_check_time";

    static int last_check_time = 0;
    private static final String TAG = NetworkUtils.class.getSimpleName();

    /*
     * @param context (to be) used to access other Utility methods
     * @return URL to query weather service
     */
    public static URL getUrl(Context context) {

        last_check_time = OfflineTutorialPreference.getLastCheckTime(context);
        Uri appQueryUri;
        if (last_check_time <=0 ) {
//            double[] preferredCoordinates = SunshinePreferences.getLocationCoordinates(context);
//            double latitude = preferredCoordinates[0];
//            double longitude = preferredCoordinates[1];
            appQueryUri = Uri.parse(OFFLINE_TUTORIAL_APP_URL).buildUpon()
                    .appendQueryParameter(INITIAL_QUERY, "true")
                    .build();
        }else {
            appQueryUri = Uri.parse(OFFLINE_TUTORIAL_APP_URL).buildUpon()
                    .appendQueryParameter(UPDATE_QUERY, "true")
                    .appendQueryParameter(LAST_CHECK_TIME, last_check_time+"")
                    .build();
        }

        try {
            URL appQueryUrl = new URL(appQueryUri.toString());
            Log.v(TAG, "URL: " + appQueryUrl);
            return appQueryUrl;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }

    }


    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response, null if no response
     * @throws IOException Related to network and stream reading
     */
    public static String getCategoriesResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            String response = null;
            if (hasInput) {
                response = scanner.next();
            }
            scanner.close();
            return response;
        } finally {
            urlConnection.disconnect();
        }
    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    public static ArrayList<ContentValues[]> getOfflineTutorialContentValuesFromJson(Context context, String jsonOfflineTutorialString)throws JSONException {
        //we should check for error code but,.. api has only ok

        ArrayList<ContentValues[]> arrayListContentValues = new ArrayList<>();

        JSONObject offlineTutorialJson = new JSONObject(jsonOfflineTutorialString);


        JSONArray jsonCategoryArray = offlineTutorialJson.getJSONArray("categories");
        JSONArray jsonSubCategoryArray = offlineTutorialJson.getJSONArray("sub_categories");
        JSONArray jsonLessonArray = offlineTutorialJson.getJSONArray("lessons");
        JSONArray jsonQuizSetArray = offlineTutorialJson.getJSONArray("quiz_set");
        JSONArray jsonQuizArray = offlineTutorialJson.getJSONArray("quiz");
        JSONArray jsonNotificationArray = offlineTutorialJson.getJSONArray("notifications");

        //
        JSONArray jsonAdmobSettingsArray = offlineTutorialJson.getJSONArray("adMobSettings");



        int last_check_time = offlineTutorialJson.getInt("last_check_time");


        final String TITLE = "title";
        final String DESCRIPTION = "description";
        final String MODIFIED_AT = "modified_at";
        final String STATUS = "status";
        final String ID ="id";

        String title;
        String description;
        int modified_at;
        int id, status;


        //handle categories data
        ContentValues[] categoryValues = new ContentValues[jsonCategoryArray.length()];

        for (int i = 0; i < jsonCategoryArray.length(); i++) {

            JSONObject categoryObject = jsonCategoryArray.getJSONObject(i);

            title = categoryObject.getString(TITLE);
            description = categoryObject.getString(DESCRIPTION);
            String icon = categoryObject.getString("icon");
            status = categoryObject.getInt(STATUS);
            modified_at = categoryObject.getInt(MODIFIED_AT);
            id = categoryObject.getInt(ID);
                ContentValues categoryValue = new ContentValues();

            categoryValue.put(OfflineTutorialContract.CategoryEntry.COLUMN_TITLE, title);
            categoryValue.put(OfflineTutorialContract.CategoryEntry.COLUMN_DESCRIPTION, description);
            categoryValue.put(OfflineTutorialContract.CategoryEntry.COLUMN_ICON, icon);
//            categoryValue.put(OfflineTutorialContract.CategoryEntry.COLUMN_ICON, Utils.getByteArrayFromUrl(icon));
            categoryValue.put(OfflineTutorialContract.CategoryEntry.COLUMN_STATUS, status);
            categoryValue.put(OfflineTutorialContract.CategoryEntry.COLUMN_MODIFIED_AT, modified_at);
            categoryValue.put(OfflineTutorialContract.CategoryEntry._ID, id);

            categoryValues[i] = categoryValue;

        }

        //handle subCategories data
        ContentValues[] subCategoryValues = new ContentValues[jsonSubCategoryArray.length()];

        for (int i = 0; i < jsonSubCategoryArray.length(); i++) {



            JSONObject subCategoryObject = jsonSubCategoryArray.getJSONObject(i);

            title = subCategoryObject.getString(TITLE);
            int category_id = subCategoryObject.getInt("category_id");
            modified_at = subCategoryObject.getInt(MODIFIED_AT);
            id = subCategoryObject.getInt(ID);
            status = subCategoryObject.getInt(STATUS);
            ContentValues subCategoryValue = new ContentValues();

            subCategoryValue.put(OfflineTutorialContract.SubCategoryEntry.COLUMN_TITLE, title);
            subCategoryValue.put(OfflineTutorialContract.SubCategoryEntry.COLUMN_CATEGORY_ID, category_id);
            subCategoryValue.put(OfflineTutorialContract.SubCategoryEntry.COLUMN_STATUS, status);
            subCategoryValue.put(OfflineTutorialContract.SubCategoryEntry.COLUMN_MODIFIED_AT, modified_at);
            subCategoryValue.put(OfflineTutorialContract.SubCategoryEntry._ID, id);

            subCategoryValues[i] = subCategoryValue;

        }

        //handle lessons data
        ContentValues[] lessonValues = new ContentValues[jsonLessonArray.length()];

        for (int i = 0; i < jsonLessonArray.length(); i++) {

            JSONObject lessonObject = jsonLessonArray.getJSONObject(i);

            title = lessonObject.getString(TITLE);
            description = lessonObject.getString(DESCRIPTION);
            int sub_category_id = lessonObject.getInt("sub_category_id");
            modified_at = lessonObject.getInt(MODIFIED_AT);
            id = lessonObject.getInt(ID);
            status = lessonObject.getInt(STATUS);
            ContentValues lessonValue = new ContentValues();

            lessonValue.put(OfflineTutorialContract.LessonEntry.COLUMN_TITLE, title);
            lessonValue.put(OfflineTutorialContract.LessonEntry.COLUMN_DESCRIPTION, description);
            lessonValue.put(OfflineTutorialContract.LessonEntry.COLUMN_SUB_CATEGORY_ID, sub_category_id);
            lessonValue.put(OfflineTutorialContract.LessonEntry.COLUMN_STATUS, status);
            lessonValue.put(OfflineTutorialContract.LessonEntry.COLUMN_MODIFIED_AT, modified_at);
            lessonValue.put(OfflineTutorialContract.LessonEntry._ID, id);

            lessonValues[i] = lessonValue;

        }

        //handle quiz data
        ContentValues[] quizValues = new ContentValues[jsonQuizArray.length()];

        for (int i = 0; i < jsonQuizArray.length(); i++) {

            JSONObject quizObject = jsonQuizArray.getJSONObject(i);

            modified_at = quizObject.getInt(MODIFIED_AT);
            id = quizObject.getInt(ID);
            String question = quizObject.getString("question");
            String answer = quizObject.getString("answer");
            String option1 = quizObject.getString("option1");
            String option2 = quizObject.getString("option2");
            String option3 = quizObject.getString("option3");
            String option4 = quizObject.getString("option4");
            int quiz_set_id = quizObject.getInt("quiz_set_id");
            status = quizObject.getInt(STATUS);

            ContentValues quizValue = new ContentValues();

            quizValue.put(OfflineTutorialContract.QuizEntry.COLUMN_QUESTION, question);
            quizValue.put(OfflineTutorialContract.QuizEntry.COLUMN_ANSWER, answer);
            quizValue.put(OfflineTutorialContract.QuizEntry.COLUMN_OPTION1, option1);
            quizValue.put(OfflineTutorialContract.QuizEntry.COLUMN_OPTION2, option2);
            quizValue.put(OfflineTutorialContract.QuizEntry.COLUMN_OPTION3, option3);
            quizValue.put(OfflineTutorialContract.QuizEntry.COLUMN_OPTION4, option4);
            quizValue.put(OfflineTutorialContract.QuizEntry.COLUMN_STATUS, status);
            quizValue.put(OfflineTutorialContract.QuizEntry.COLUMN_MODIFIED_AT, modified_at);
            quizValue.put(OfflineTutorialContract.QuizEntry.COLUMN_QUIZ_SET_ID, quiz_set_id);
            quizValue.put(OfflineTutorialContract.QuizEntry._ID, id);

            quizValues[i] = quizValue;

        }

        //handle notificationdata
        ContentValues[] notificationValues = new ContentValues[jsonNotificationArray.length()];

        for (int i = 0; i < jsonNotificationArray.length(); i++) {

            JSONObject notificationObject = jsonNotificationArray.getJSONObject(i);

            modified_at = notificationObject.getInt(MODIFIED_AT);
            id = notificationObject.getInt(ID);
            title = notificationObject.getString("title");
            String message = notificationObject.getString("message");
            String link = notificationObject.getString("link");
            String image = notificationObject.getString("image");

            ContentValues notificationValue = new ContentValues();

            notificationValue.put(OfflineTutorialContract.NotificationEntry.COLUMN_TITLE, title);
            notificationValue.put(OfflineTutorialContract.NotificationEntry.COLUMN_MESSAGE, message);
            notificationValue.put(OfflineTutorialContract.NotificationEntry.COLUMN_LINK, link);
            notificationValue.put(OfflineTutorialContract.NotificationEntry.COLUMN_IMAGE, image);
            notificationValue.put(OfflineTutorialContract.NotificationEntry.COLUMN_MODIFIED_AT, modified_at);
            notificationValue.put(OfflineTutorialContract.NotificationEntry._ID, id);

            notificationValues[i] = notificationValue;

        }

        //handle quizset data
        ContentValues[] quizSetValues = new ContentValues[jsonQuizSetArray.length()];

        for (int i = 0; i < jsonQuizSetArray.length(); i++) {

            JSONObject quizSetObject = jsonQuizSetArray.getJSONObject(i);

            modified_at = quizSetObject.getInt(MODIFIED_AT);
            id = quizSetObject.getInt(ID);
            String name = quizSetObject.getString("name");
            status = quizSetObject.getInt(STATUS);


            ContentValues quizSetValue = new ContentValues();

            quizSetValue.put(OfflineTutorialContract.QuizSetEntry.COLUMN_NAME, name);
            quizSetValue.put(OfflineTutorialContract.QuizSetEntry.COLUMN_STATUS, status);
            quizSetValue.put(OfflineTutorialContract.QuizSetEntry.COLUMN_MODIFIED_AT, modified_at);
            quizSetValue.put(OfflineTutorialContract.QuizSetEntry._ID, id);

            quizSetValues[i] = quizSetValue;

        }

        arrayListContentValues.add(categoryValues);
        arrayListContentValues.add(subCategoryValues);
        arrayListContentValues.add(lessonValues);
        arrayListContentValues.add(quizValues);
        arrayListContentValues.add(notificationValues);
        arrayListContentValues.add(quizSetValues);





        //it needs not be an arra, yes, but the api sends array ):
        for(int i = 0; i<jsonAdmobSettingsArray.length(); i++){
            String publisherId, bannerId, interstitialId;

            JSONObject adMob = jsonAdmobSettingsArray.getJSONObject(i);
            publisherId = adMob.getString("publisher_id");
            bannerId = adMob.getString("banner_ad_id");
            interstitialId = adMob.getString("interstitial_ad_id");

            OfflineTutorialPreference.setPref(context, publisherId, bannerId, interstitialId);

        }

        //last check time
        OfflineTutorialPreference.setPrefLastCheckTime(context,last_check_time);



        return arrayListContentValues;
    }

    public static JSONObject getInfo() throws JSONException {
        String response;
        try {
            //yeah it says getCat but it just a name,its getting info
            response = getCategoriesResponseFromHttpUrl(new URL(OFFLINE_TUTORIAL_APP_URL));

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return  new JSONObject(response);

    }
}
