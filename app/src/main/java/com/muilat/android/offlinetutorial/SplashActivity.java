package com.muilat.android.offlinetutorial;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.ads.MobileAds;
import com.muilat.android.offlinetutorial.pref.OfflineTutorialPreference;
import com.muilat.android.offlinetutorial.sync.OfflineTutorialSyncIntentService;
import com.muilat.android.offlinetutorial.sync.OfflineTutorialSyncUtils;

import static java.lang.Thread.sleep;

public class SplashActivity extends Activity {
    private TextView app_name;
    private ImageView logo;
    private ProgressBar progressBar;

    public static ContentResolver contentResolver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        app_name = findViewById(R.id.app_name);
        logo = findViewById(R.id.logo);
        progressBar = findViewById(R.id.progressBar);

        contentResolver = getContentResolver();

        //initialize Admob
        MobileAds.initialize(this, OfflineTutorialPreference.getPublisherId(this));

        TextView textView =  findViewById(R.id.textView2);
        final String link = textView.getText().toString();

        SpannableString content = new SpannableString(link);
        content.setSpan(new UnderlineSpan(), 24, link
                .length(), 0);
        content.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.linkedin.com/in/muilat")));}
        }, 24, link.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(content);
        textView.setMovementMethod(LinkMovementMethod.getInstance());

        if(savedInstanceState == null){

            OfflineTutorialSyncUtils.initialize(this);
            Intent intentToSyncImmediately = new Intent(this, OfflineTutorialSyncIntentService.class);
            startService(intentToSyncImmediately);



            Animation transition = AnimationUtils.loadAnimation(this, R.anim.transition);
            app_name.startAnimation(transition);

            Animation fromTop = AnimationUtils.loadAnimation(SplashActivity.this, R.anim.fromtop);
            logo.startAnimation(fromTop);

//            Animation loading = AnimationUtils.loadAnimation(this, R.anim.transition);
//            loading.setStartTime(5000);
//            progressBar.startAnimation(loading);

            final Intent mainActivityIntent = new Intent(this, MainActivity.class);


            final Thread splashTimer = new Thread() {
                @Override
                public void run() {

                    try{
                        sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    finally {
                        startActivity(mainActivityIntent);
                        finish();
                    }
                }
            };
            splashTimer.start();




        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
