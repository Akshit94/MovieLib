package com.example.jainsaab.movielib;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.KeyEvent;

import com.example.jainsaab.movielib.pagerAdapters.SectionsPagerAdapter;
import com.example.jainsaab.movielib.utility.Constants;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

public class MoviesActivity extends AppCompatActivity {

    SharedPreferences getPrefs;
    InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);

        getPrefs = PreferenceManager
                .getDefaultSharedPreferences(getBaseContext());
        String session = getPrefs.getString(Constants.SESSION_SHARED_PREFS, null);
        if(session == null){
            startActivity(new Intent(getApplicationContext(), SignInActivity.class));
            finish();
        }

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                finish();
            }
        });
        requestNewInterstitial();

        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        /*
      The {@link android.support.v4.view.PagerAdapter} that will provide
      fragments for each of the sections. We use a
      {@link FragmentPagerAdapter} derivative, which will keep every
      loaded fragment in memory. If this becomes too memory intensive, it
      may be best to switch to a
      {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), getApplicationContext());

        // Set up the ViewPager with the sections adapter.
        /*
      The {@link ViewPager} that will host the section contents.
     */
        ViewPager mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        SharedPreferences.Editor e = getPrefs.edit();
        e.putInt(Constants.POPULAR_MOVIES_TASK_FLAG, 0);
        e.putInt(Constants.UPCOMING_MOVIES_TASK_FLAG, 0);
        e.putInt(Constants.FAVOURITE_MOVIES_TASK_FLAG, 0);
        e.apply();

    }

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("EA919A44636A54C081E03B510D78765C")
                .build();

        mInterstitialAd.loadAd(adRequest);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mInterstitialAd.isLoaded()) {
                mInterstitialAd.show();
            }
        }
            return super.onKeyDown(keyCode, event);
    }
}
