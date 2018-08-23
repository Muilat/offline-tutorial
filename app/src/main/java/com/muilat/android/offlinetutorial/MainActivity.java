package com.muilat.android.offlinetutorial;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.LayerDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ShareCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.muilat.android.offlinetutorial.data.Categories;
import com.muilat.android.offlinetutorial.data.OfflineTutorialContract;
import com.muilat.android.offlinetutorial.data.OfflineTutorialDbHelper;
import com.muilat.android.offlinetutorial.data.SubCategories;
import com.muilat.android.offlinetutorial.adapter.SubCategoryAdapter;
import com.muilat.android.offlinetutorial.pref.OfflineTutorialPreference;
import com.muilat.android.offlinetutorial.sync.OfflineTutorialSyncIntentService;
import com.muilat.android.offlinetutorial.sync.OfflineTutorialSyncUtils;
import com.muilat.android.offlinetutorial.util.NetworkUtils;
import com.muilat.android.offlinetutorial.util.Utils;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = MainActivity.class.getSimpleName();


    public static FragmentTransaction fragmentTransaction;
    public static FragmentManager fragmentManager;


    private int mNotificationsCount = 0;
    private SQLiteDatabase mDb;

    private OfflineTutorialDbHelper dbHelper;


    private long backPressedTime;

    InterstitialAd interstitialAd;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        FloatingActionButton fab = findViewById(R.id.quiz_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog quizDialog = new Dialog(MainActivity.this);

                quizDialog.setContentView(R.layout.quiz_start_dialog);
                ImageView closeDialog = quizDialog.findViewById(R.id.close_dialog);
                Button confrimStart = quizDialog.findViewById(R.id.confirm);
//        TextView dialogMessage = quizDialog.findViewById(R.id.dialog_message);

//        confrimExit.setText(R.string.exit);
//        dialogMessage.setText(R.string.exit_message);


                closeDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        quizDialog.dismiss();
                        Toast.makeText(MainActivity.this, "Check back later when you are bold enough",Toast.LENGTH_SHORT).show();

                    }
                });

                confrimStart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        quizDialog.dismiss();
                        Intent  quizIntent = new Intent(MainActivity.this, QuizActivity.class);
                        startActivity(quizIntent);

                    }
                });

//        quizDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                quizDialog.show();


            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, new CategoryFragment());

        fragmentTransaction.commit();



        dbHelper = new OfflineTutorialDbHelper(this);
        mDb= dbHelper.getReadableDatabase();

        if(savedInstanceState == null){
//            OfflineTutorialSyncUtils.initialize(this);
//            Intent intentToSyncImmediately = new Intent(this, OfflineTutorialSyncIntentService.class);
//            startService(intentToSyncImmediately);
//TODO:BE back here
        }

        //adView
        LinearLayout adViewLinearLayout = findViewById(R.id.adViewLayout);
        Utils.loadAdView(this, adViewLinearLayout);




        //interstitialAd
        interstitialAd = new InterstitialAd(MainActivity.this);
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                Log.e(TAG, "ad closed");
                interstitialAd.loadAd(new AdRequest.Builder().build());
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                super.onAdFailedToLoad(errorCode);
                Log.e(TAG, "ad failed");

            }

            @Override
            public void onAdLeftApplication() {
                super.onAdLeftApplication();
                Log.e(TAG, "ad left application");

            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
                Log.e(TAG, "ad opened");

            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                Log.e(TAG, "ad loaded");

            }
        });

        //interstitialAd
        Handler handlerInterstitialAd = new Handler();
        handlerInterstitialAd.postDelayed(new Runnable() {
            @Override
            public void run() {
                interstitialAd.setAdUnitId(OfflineTutorialPreference.getInterstitialId(MainActivity.this));

                interstitialAd.loadAd(new AdRequest.Builder().build());
            }
        },3000);

    }


    public void onCategoryClicked(View view){
//        int category_id = (int) view.findViewById(R.id.category_card).getTag();
        Categories category = (Categories) view.findViewById(R.id.category_card).getTag();

        Intent subCategoryFragment = new Intent(MainActivity.this, SubCategoryFragment.class);

        subCategoryFragment.putExtra(SubCategoryFragment.ARG_CATEGORY_ID,category.getId());
        subCategoryFragment.putExtra(SubCategoryFragment.ARG_CATEGORY_TITLE,category.getTitle());
//        subCategoryFragment.setArguments(args);
//
//        fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.container,subCategoryFragment );
//        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
//        fragmentTransaction.addToBackStack(null);
//        fragmentTransaction.commit();

        startActivity(subCategoryFragment);
    }


    @Override
    protected void onResume() {
        super.onResume();

        //fecth notification ccount
        new FetchCountTask().execute();



    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

                if (backPressedTime + 2000 > System.currentTimeMillis()) {
                    showInterstitialAd();

                    finish();
                } else {
                    Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();
                }

                backPressedTime = System.currentTimeMillis();

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        // Get the notifications MenuItem and
        // its LayerDrawable (layer-list)
        MenuItem item = menu.findItem(R.id.action_notification);
        LayerDrawable icon = (LayerDrawable) item.getIcon();

        // Update LayerDrawable's BadgeDrawable
        Utils.setBadgeCount(this, icon, mNotificationsCount);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            showInterstitialAd();

            Intent searchIntent = new Intent(this, SearchActivity.class);
            startActivity(searchIntent);
            return true;
        }
        if (id == R.id.action_notification) {
            showInterstitialAd();

            Intent notificationIntent = new Intent(this, NotificationActivity.class);
            startActivity(notificationIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_favourites) {
            showInterstitialAd();

            // Handle the favourites action
            Intent favouriteIntent = new Intent(this, FavouriteActivity.class);
            startActivity(favouriteIntent);
            return true;
        } else if (id == R.id.nav_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        } else if (id == R.id.nav_share) {
            String textToShare = "Checkout "+getString(R.string.app_name);
            textToShare += "\nInstall from http://play.google.com/store/apps/details?id=" + getPackageName();

            Intent shareIntent = ShareCompat.IntentBuilder.from(MainActivity.this)
                    .setText(textToShare)
                    .setChooserTitle("Share Developer with")
                    .setSubject(getString(R.string.app_name))
                    .setType("text/plain")
                    .createChooserIntent();

            if(shareIntent.resolveActivity(getPackageManager()) != null){
                startActivity(shareIntent);
            }
        } else if (id == R.id.nav_send) {

        } else if (id == R.id.nav_rate_app) {

            try {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=" + getPackageName())));
            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
            }
        }else if(id==R.id.nav_about){
            showInterstitialAd();
            Intent intent = new Intent(MainActivity.this, InfoActivity.class);
            intent.putExtra(InfoActivity.EXTRA_INFO_TYPE, InfoActivity.EXTRA_INFO_TYPE_ABOUT);
            startActivity(intent);
        }else if(id==R.id.nav_policy){
            showInterstitialAd();
            Intent intent = new Intent(MainActivity.this, InfoActivity.class);
            intent.putExtra(InfoActivity.EXTRA_INFO_TYPE, InfoActivity.EXTRA_INFO_TYPE_PRIVACY_POLICY);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /*
  AsyncTask to fetch the notifications count
  */
    class FetchCountTask extends AsyncTask<Void, Void, Integer> {

        @Override
        protected Integer doInBackground(Void... params) {
            String countQuery = "SELECT  * FROM " + OfflineTutorialContract.NotificationEntry.TABLE_NAME+" WHERE status = 1";
            Cursor cursor = mDb.rawQuery(countQuery, null);
            int count = cursor.getCount();
            cursor.close();

            return count;
        }

        @Override
        public void onPostExecute(Integer count) {
            updateNotificationsBadge(count);

        }
    }

    private void updateNotificationsBadge(int count) {
        mNotificationsCount = count;


        // force the ActionBar to relayout its MenuItems.
        // onCreateOptionsMenu(Menu) will be called again.
        invalidateOptionsMenu();
    }

    private void showInterstitialAd(){
        if(interstitialAd != null && interstitialAd.isLoaded()){
            interstitialAd.show();
        }
    }



    @Override
    public void onSaveInstanceState(Bundle outState) {
//        outState.putBoolean("data_fetched", data_fetched);
        super.onSaveInstanceState(outState);
    }

}
