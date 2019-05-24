package com.muilat.android.offlinetutorial;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import static android.content.Intent.ACTION_CALL;

public class DeveloperActivity extends AppCompatActivity {

    EditText message_edt;
//    Button mess

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developer);

        message_edt = findViewById(R.id.message_edt);


    }


    public void sendDeveloperMessage(View view) {
        if (!message_edt.getText().toString().equals("")) {
            // Use an intent to launch an email app.
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:")); // only email apps should handle this
            intent.putExtra(Intent.EXTRA_SUBJECT, "Message from Offline Tutorial App user");
            intent.putExtra(Intent.EXTRA_TEXT, message_edt.getText().toString());
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"muilat.champ@gmail.com"});

            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);

            }

        } else {
            message_edt.setVisibility(View.VISIBLE);


        }


    }


    public void linkedin(View view) {
        startActivity(new Intent(Intent.ACTION_VIEW,
                Uri.parse("https://www.linkedin.com/in/muilat")));
    }

    public void github(View view) {
        startActivity(new Intent(Intent.ACTION_VIEW,
                Uri.parse("https://www.github.com/muilat")));
    }

    public void phone(View view) {

        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:+2348060628278"));
        if(intent.resolveActivity(getPackageManager()) !=  null)
            startActivity(intent);
    }
}
