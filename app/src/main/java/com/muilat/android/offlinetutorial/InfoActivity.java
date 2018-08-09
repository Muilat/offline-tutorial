package com.muilat.android.offlinetutorial;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.muilat.android.offlinetutorial.util.NetworkUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

public class InfoActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<JSONObject> {

    private static final String TAG = InfoActivity.class.getSimpleName();
    TextView emptyTextView, infoTextView;
    LinearLayout emptyLayout;
    ProgressBar progressBar;

    public static final int EXTRA_INFO_TYPE_ABOUT = 1;
    public static final int EXTRA_INFO_TYPE_PRIVACY_POLICY = 2;
    public static final int EXTRA_INFO_TYPE_PUBLISHER_ID = 3;

    public static final String EXTRA_INFO_TYPE = "extra-info-type";

    int infoType;
    String info = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        emptyLayout = findViewById(R.id.emptyLayout);
        emptyTextView = findViewById(R.id.emptyText);
        infoTextView = findViewById(R.id.info_text);
        progressBar = findViewById(R.id.progress_bar);

        if(savedInstanceState == null){
            checkNetworkFetch();

        }
        else
        {
            info = savedInstanceState.getString("info");
            if (info.equals("")){
                checkNetworkFetch();

            }{
                progressBar.setVisibility(View.GONE);
                if(Build.VERSION.SDK_INT<Build.VERSION_CODES.N){
                    infoTextView.setText(Html.fromHtml(info));

                }
                else {
                    infoTextView.setText(Html.fromHtml(info,Html.FROM_HTML_MODE_COMPACT));
                }
            }
        }




    }

    private void checkNetworkFetch() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isAvailable() &&  networkInfo.isConnected()){
            //network is available go get the data
            getSupportLoaderManager().initLoader(1, null, InfoActivity.this);

            infoType = getIntent().getIntExtra(EXTRA_INFO_TYPE,0);
        }
        else{
            //hide progress bar
            progressBar.setVisibility(View.GONE);
            //internet is not available; make it known
            //make it visible
            emptyTextView.setVisibility(View.VISIBLE);
            emptyTextView.setText(R.string.no_internet);
            emptyLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public Loader<JSONObject> onCreateLoader(int id, Bundle args) {
        return new android.support.v4.content.AsyncTaskLoader<JSONObject>(this) {

            // Initialize a JSONObject, this will hold all the Category data
//            JSONObject mCategoriesData = null;
            JSONObject mLessonsData;

            // onStartLoading() is called when a loader first starts loading data
            @Override
            protected void onStartLoading() {
                if (mLessonsData != null) {
                    // Delivers any previously loaded data immediately
                    deliverResult(mLessonsData);
                } else {
                    // Force a new load
                    forceLoad();
                }
            }

            // loadInBackground() performs asynchronous loading of data
            @Override
            public JSONObject loadInBackground() {

                try {
                    JSONObject infoObject = NetworkUtils.getInfo();
                    return infoObject;
                } catch (JSONException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            public void deliverResult(JSONObject data) {
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<JSONObject> loader, JSONObject data) {
         Log.e(TAG, "reurns "+data.toString());

        progressBar.setVisibility(View.GONE);

        try {
            JSONObject value = data.getJSONArray("Offline_tutorial_app").getJSONObject(0);
            switch (infoType) {
                case EXTRA_INFO_TYPE_ABOUT:
                    info = value.getString("app_description");
                    break;
                case EXTRA_INFO_TYPE_PRIVACY_POLICY:
                    info = value.getString("app_privacy_policy");
                    break;
                default:
                    break;
            }

            if(info.equals("")){
                infoTextView.setVisibility(View.GONE);
                emptyLayout.setVisibility(View.VISIBLE);
            }
            else{
                if(Build.VERSION.SDK_INT<Build.VERSION_CODES.N){
                    infoTextView.setText(Html.fromHtml(info));

                }
                else {
                    infoTextView.setText(Html.fromHtml(info,Html.FROM_HTML_MODE_COMPACT));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            infoTextView.setVisibility(View.GONE);
            emptyLayout.setVisibility(View.VISIBLE);
        }


    }


    @Override
    public void onLoaderReset(Loader<JSONObject>loader) {
        progressBar.setVisibility(View.VISIBLE);
    }



    public class InfoLoader extends AsyncTaskLoader<JSONObject>{

        public InfoLoader(Context context) {
            super(context);
        }

        @Override
        public JSONObject loadInBackground() {
            try {
                JSONObject infoObject = NetworkUtils.getInfo();
                return infoObject;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }

        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("info", info);
        super.onSaveInstanceState(outState);
    }
}
