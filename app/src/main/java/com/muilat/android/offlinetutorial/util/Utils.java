package com.muilat.android.offlinetutorial.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.muilat.android.offlinetutorial.MainActivity;
import com.muilat.android.offlinetutorial.R;
import com.muilat.android.offlinetutorial.pref.OfflineTutorialPreference;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;

public class Utils {

    private static final String TAG = "Utils";
    public static Random RANDOM = new Random();


    public static int generateColor(){
        int red = RANDOM.nextInt(255);//255  to maximize chances
        int green = RANDOM.nextInt(255);
        int blue = RANDOM.nextInt(255);

        return Color.rgb(red, green, blue);

    }



    /*
      AsyncTask to fetch the Ads settings
      */

    public static void loadAdView(final Context context, final LinearLayout mAdviewLinearLayout) {
        final AdView adView = new AdView(context);

        final Handler handlerAdView = new Handler();
        handlerAdView.postDelayed(new Runnable() {
            @Override
            public void run() {

                adView.setAdSize(AdSize.BANNER);
                adView.setAdUnitId(OfflineTutorialPreference.getBannerId(context));
                final AdRequest adRequest = new AdRequest.Builder()
                        .build();
                //load Admob
                adView.loadAd(adRequest);
                adView.bringToFront();

                //add adView to the linearlayout
                mAdviewLinearLayout.addView(adView);
                adView.setAdListener(new AdListener() {
                    @Override
                    public void onAdFailedToLoad(int errorCode) {
                        super.onAdFailedToLoad(errorCode);
                        mAdviewLinearLayout.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAdLoaded() {
                        super.onAdLoaded();
                        mAdviewLinearLayout.setVisibility(View.VISIBLE);

                    }

                });
            }
        },3000);
    }



    public static byte[] getByteArrayFromUrl(String src){
        try{

            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream inputStream = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();

            return data;
        }catch (IOException e){
            e.printStackTrace();
            return null;

        }

    }

    public Bitmap byteArrayToBit(byte[] blob){
        Bitmap bitmap = BitmapFactory.decodeByteArray(blob, 0, blob.length);
        return bitmap;
    }

    public static void setBadgeCount(Context context, LayerDrawable icon, int count) {

        BadgeDrawable badge;

        // Reuse drawable if possible
        Drawable reuse = icon.findDrawableByLayerId(R.id.ic_badge);
        if (reuse != null && reuse instanceof BadgeDrawable) {
            badge = (BadgeDrawable) reuse;
        } else {
            badge = new BadgeDrawable(context);
        }

        badge.setCount(count);
        icon.mutate();
        icon.setDrawableByLayerId(R.id.ic_badge, badge);
    }
}
