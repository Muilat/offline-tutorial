package com.muilat.android.offlinetutorial.pref;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;

public class OfflineTutorialPreference {

    public static final String PREF_LAST_CHECK_TIME = "last_check_time";
    public static final String PREF_PUBLISER_ID = "publisher_id";
    public static final String PREF_BANNER_ID = "banner_id";
    public static final String PREF_INTERSTITIAI_ID = "interstitial_id";

    static int default_last_check_time =0;
    static  String default_publisher_id = "ca-app-pub-3940256099942544~3347511713";
    static  String default_banner_id = "ca-app-pub-3940256099942544/6300978111";
    static  String default_interstitial_id = "ca-app-pub-3940256099942544/1033173712";

    public static void setPrefLastCheckTime(Context context, int last_check_time) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();

        editor.putInt(PREF_LAST_CHECK_TIME, last_check_time);
    }

    public static void setPref(Context context, String publisher_id, String banner_id, String interstitial_id) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();

        editor.putString(PREF_PUBLISER_ID, publisher_id);
        editor.putString(PREF_BANNER_ID, banner_id);
        editor.putString(PREF_INTERSTITIAI_ID, interstitial_id);
        editor.apply();
    }

    public static int getLastCheckTime(Context context){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);

        return sp.getInt(PREF_LAST_CHECK_TIME, default_last_check_time);
    }

    public static String getPublisherId(Context context){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);

        return sp.getString(PREF_PUBLISER_ID, default_publisher_id);
    }

    public static String getBannerId(Context context){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);

        return sp.getString(PREF_BANNER_ID, default_banner_id);
    }

    public static String getInterstitialId(Context context){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);

        return sp.getString(PREF_INTERSTITIAI_ID, default_interstitial_id);
    }


}
