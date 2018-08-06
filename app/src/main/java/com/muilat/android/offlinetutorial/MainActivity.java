package com.muilat.android.offlinetutorial;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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

import com.muilat.android.offlinetutorial.data.OfflineTutorialContract;
import com.muilat.android.offlinetutorial.data.SubCategories;
import com.muilat.android.offlinetutorial.data.SubCategoryAdapter;
import com.muilat.android.offlinetutorial.sync.OfflineTutorialSyncIntentService;
import com.muilat.android.offlinetutorial.sync.OfflineTutorialSyncUtils;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    public static FragmentTransaction fragmentTransaction;
    public static FragmentManager fragmentManager;

    public static ContentResolver contentResolver;


    RecyclerView selected_category;

    private int mLongAnimationDuration;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.quiz_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                Intent  quizIntent = new Intent(MainActivity.this, QuizActivity.class);
//                quizIntent.pu
                startActivity(quizIntent);
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

        //retrieve and cache the system's default"short animation time
        mLongAnimationDuration = getResources().getInteger(android.R.integer.config_longAnimTime);

        ///initialize SynAdapter
//        OfflineTutorialSyncAdapter.initializeSyncAdapter(this);

        contentResolver = getContentResolver();

//        OfflineTutorialSyncUtils.initialize(this);
        Intent intentToSyncImmediately = new Intent(this, OfflineTutorialSyncIntentService.class);
        startService(intentToSyncImmediately);

    }


    public void onCategoryClicked(View view){
//        subCat_holder = view.findViewById(R.id.sub_cat_holder);

        RecyclerView subCat_recyclerView = view.findViewById(R.id.sub_cat_recyclerView);
        int category_id = (int) view.findViewById(R.id.category_card).getTag();

        if(selected_category !=null){
            //a view alredy shown, deshown
//            selected_category.animate()
//                    .alpha(0f)
//                    .setDuration(mLongAnimationDuration)
//                    .setListener(new Animator.AnimatorListener() {
//                        @Override
//                        public void onAnimationStart(Animator animator) {
//
//                        }
//
//                        @Override
//                        public void onAnimationEnd(Animator animator) {
//                            selected_category.setVisibility(View.GONE);
//                        }
//
//                        @Override
//                        public void onAnimationCancel(Animator animator) {
//                            selected_category.setVisibility(View.GONE);
//
//                        }
//
//                        @Override
//                        public void onAnimationRepeat(Animator animator) {
//                        }
//                    });
            subCat_recyclerView.setVisibility(View.GONE);
            if (selected_category == subCat_recyclerView){
                selected_category = null;
                return;
            }

            selected_category.setVisibility(View.GONE);
        }



        subCat_recyclerView.setAlpha(0f); //0% opacity
        subCat_recyclerView.setVisibility(View.VISIBLE);

        //animate, set opacity to 100% and clear listener
        subCat_recyclerView.animate()
                .alpha(1f)
                .setDuration(mLongAnimationDuration)
                .setListener(null);

        selected_category = subCat_recyclerView;


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        subCat_recyclerView.setLayoutManager(layoutManager);

        SubCategoryAdapter mSubCategoryAdapter;
        mSubCategoryAdapter = new SubCategoryAdapter();
        subCat_recyclerView.setItemAnimator(new DefaultItemAnimator()); //Animator for recycler view
        subCat_recyclerView.setAdapter(mSubCategoryAdapter);
        subCat_recyclerView.setHasFixedSize(true);

        //Todo: change to TaskLoader
        String stringId = Long.toString(category_id);
        Uri uri = OfflineTutorialContract.SubCategoryEntry.CONTENT_URI;
        uri = uri.buildUpon().appendPath(stringId).build();


        Cursor cursor = getContentResolver().query(uri,
                null,
                null,
                null,
                null);
        mSubCategoryAdapter.swapCursor(cursor);
        Log.e(TAG, "No of SubCategories: "+cursor.getCount());





    }

    public void onSubCategoryClicked(View view){

        SubCategories category = (SubCategories) view.findViewById(R.id.sub_cat_card).getTag();


        //go to details
        Intent detailsIntent = new Intent(this, LessonActivity.class);
        detailsIntent.putExtra(LessonActivity.EXTRA_SUB_CATEGORY, category);
        startActivity(detailsIntent);
                //Get words
//        SubCategoryFragment commonFragment = new SubCategoryFragment();
//        Bundle args = new Bundle();
//        args.putInt(SubCategoryFragment.ARG_CATEGORY_ID,category_id);
//        commonFragment.setArguments(args);
//
//        fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.sub_cat_holder,commonFragment );
//        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
//        fragmentTransaction.addToBackStack(null);
//        fragmentTransaction.commit();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
            Intent searchIntent = new Intent(this, SearchActivity.class);
            startActivity(searchIntent);
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
            // Handle the favourites action
            Intent favouriteIntent = new Intent(this, FavouriteActivity.class);
            startActivity(favouriteIntent);
            return true;
        } else if (id == R.id.nav_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
