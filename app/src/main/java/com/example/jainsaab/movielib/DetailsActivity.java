package com.example.jainsaab.movielib;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.jainsaab.movielib.pagerAdapters.DetailsSectionsPagerAdapter;
import com.example.jainsaab.movielib.utility.Constants;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setTitle(getIntent().getStringExtra(Constants.TMDB_TITLE));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        Bundle args = new Bundle();

        args.putString(Constants.TMDB_TITLE, intent.getStringExtra(Constants.TMDB_TITLE));
        args.putString(Constants.TMDB_RELEASE_DATE, intent.getStringExtra(Constants.TMDB_RELEASE_DATE));
        args.putString(Constants.TMDB_VOTE_AVG, intent.getStringExtra(Constants.TMDB_VOTE_AVG));
        args.putString(Constants.TMDB_OVERVIEW, intent.getStringExtra(Constants.TMDB_OVERVIEW));
        args.putString(Constants.TMDB_ADULT, intent.getStringExtra(Constants.TMDB_ADULT));

        if (!intent.hasExtra(Constants.TMDB_ACTOR_NAME_ARGUMENT)) {
            args.putString(Constants.TMDB_BACKDROP, intent.getStringExtra(Constants.TMDB_BACKDROP));
            args.putString(Constants.TMDB_POSTER, intent.getStringExtra(Constants.TMDB_POSTER));
            args.putIntArray(Constants.TMDB_GENRES, intent.getIntArrayExtra(Constants.TMDB_GENRES));
            args.putString(Constants.TMDB_MOVIE_ID, intent.getStringExtra(Constants.TMDB_MOVIE_ID));

        } else {
            args.putString(Constants.TMDB_GENRES_STRING, intent.getStringExtra(Constants.TMDB_GENRES_STRING));
            args.putInt(Constants.TMDB_MOVIE_ID, intent.getIntExtra(Constants.TMDB_MOVIE_ID, 0));
            args.putStringArray(Constants.TMDB_AUTHOR, intent.getStringArrayExtra(Constants.TMDB_AUTHOR));
            args.putStringArray(Constants.TMDB_CONTENT, intent.getStringArrayExtra(Constants.TMDB_CONTENT));
            args.putStringArray(Constants.TMDB_REVIEW_URL, intent.getStringArrayExtra(Constants.TMDB_REVIEW_URL));
            args.putStringArray(Constants.TMDB_CHARACTER_NAME, intent.getStringArrayExtra(Constants.TMDB_CHARACTER_NAME));
            args.putStringArray(Constants.TMDB_ACTOR_NAME_ARGUMENT, intent.getStringArrayExtra(Constants.TMDB_ACTOR_NAME_ARGUMENT));
            args.putStringArray(Constants.TMDB_TRAILER_NAME, intent.getStringArrayExtra(Constants.TMDB_TRAILER_NAME));
            args.putStringArray(Constants.TMDB_TRAILER_KEY, intent.getStringArrayExtra(Constants.TMDB_TRAILER_KEY));
        }

        DetailsSectionsPagerAdapter mDetailsSectionsPagerAdapter = new DetailsSectionsPagerAdapter(getSupportFragmentManager(), args, getApplicationContext());
        // Set up the ViewPager with the sections adapter.
        ViewPager mViewPager = (ViewPager) findViewById(R.id.fragment_details_container);
        mViewPager.setAdapter(mDetailsSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.detail_tabs);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                supportFinishAfterTransition();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
