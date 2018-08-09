package com.muilat.android.offlinetutorial;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashActivity extends Activity {
    private TextView app_name;
    private ImageView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        app_name = findViewById(R.id.app_name);
        logo = findViewById(R.id.logo);


        if(savedInstanceState == null){
            Animation transition = AnimationUtils.loadAnimation(this, R.anim.transition);
            app_name.startAnimation(transition);

//            Animation fromTop = AnimationUtils.loadAnimation(this, R.anim.fromtop);
//            logo.startAnimation(fromTop);

            final Intent mainActivityIntent = new Intent(this, MainActivity.class);

            Thread splashTimer = new Thread() {
                @Override
                public void run() {
                    Animation fromTop = AnimationUtils.loadAnimation(SplashActivity.this, R.anim.fromtop);
                    logo.startAnimation(fromTop);

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
