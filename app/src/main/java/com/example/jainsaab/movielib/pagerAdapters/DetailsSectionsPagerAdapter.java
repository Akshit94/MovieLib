package com.example.jainsaab.movielib.pagerAdapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.jainsaab.movielib.DetailsActivityFragment;
import com.example.jainsaab.movielib.R;
import com.example.jainsaab.movielib.cast.CastFragment;
import com.example.jainsaab.movielib.reviews.ReviewFragment;
import com.example.jainsaab.movielib.trailers.TrailerFragment;
import com.example.jainsaab.movielib.utility.Constants;

public class DetailsSectionsPagerAdapter extends FragmentPagerAdapter{
    private Bundle mArguments;
    private Context mContext;

    public DetailsSectionsPagerAdapter(FragmentManager fm, Bundle args, Context context) {
        super(fm);
        mArguments = args;
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        if(position == 0){
            DetailsActivityFragment detailsActivityFragment = new DetailsActivityFragment();
            detailsActivityFragment.setArguments(mArguments);
            return detailsActivityFragment;
        } else if (position == 1){
            CastFragment castFragment = new CastFragment();
            Bundle args = new Bundle();
            args = putInArgs(args);
            castFragment.setArguments(args);
            return castFragment;
        } else if(position == 2){
            TrailerFragment trailerFragment = new TrailerFragment();
            Bundle args = new Bundle();
            args = putInArgs(args);
            trailerFragment.setArguments(args);
            return trailerFragment;
        } else if(position == 3){
            ReviewFragment reviewFragment = new ReviewFragment();
            Bundle args = new Bundle();
            args = putInArgs(args);
            reviewFragment.setArguments(args);
            return reviewFragment;
        }
        return null;
    }

    private Bundle putInArgs(Bundle args){
        args.putString(Constants.TMDB_TITLE, mArguments.getString(Constants.TMDB_TITLE));
        args.putString(Constants.TMDB_RELEASE_DATE, mArguments.getString(Constants.TMDB_RELEASE_DATE));
        args.putString(Constants.TMDB_VOTE_AVG, mArguments.getString(Constants.TMDB_VOTE_AVG));
        args.putString(Constants.TMDB_OVERVIEW, mArguments.getString(Constants.TMDB_OVERVIEW));
        args.putString(Constants.TMDB_ADULT, mArguments.getString(Constants.TMDB_ADULT));
        if (!mArguments.containsKey(Constants.TMDB_ACTOR_NAME_ARGUMENT)) {
            args.putString(Constants.TMDB_BACKDROP, mArguments.getString(Constants.TMDB_BACKDROP));
            args.putString(Constants.TMDB_POSTER, mArguments.getString(Constants.TMDB_POSTER));
            args.putIntArray(Constants.TMDB_GENRES, mArguments.getIntArray(Constants.TMDB_GENRES));
            args.putString(Constants.TMDB_MOVIE_ID, mArguments.getString(Constants.TMDB_MOVIE_ID));

        } else {
            args.putString(Constants.TMDB_GENRES_STRING, mArguments.getString(Constants.TMDB_GENRES_STRING));
            args.putInt(Constants.TMDB_MOVIE_ID, mArguments.getInt(Constants.TMDB_MOVIE_ID, 0));
            args.putStringArray(Constants.TMDB_AUTHOR, mArguments.getStringArray(Constants.TMDB_AUTHOR));
            args.putStringArray(Constants.TMDB_CONTENT, mArguments.getStringArray(Constants.TMDB_CONTENT));
            args.putStringArray(Constants.TMDB_REVIEW_URL, mArguments.getStringArray(Constants.TMDB_REVIEW_URL));
            args.putStringArray(Constants.TMDB_CHARACTER_NAME, mArguments.getStringArray(Constants.TMDB_CHARACTER_NAME));
            args.putStringArray(Constants.TMDB_ACTOR_NAME_ARGUMENT, mArguments.getStringArray(Constants.TMDB_ACTOR_NAME_ARGUMENT));
            args.putStringArray(Constants.TMDB_TRAILER_NAME, mArguments.getStringArray(Constants.TMDB_TRAILER_NAME));
            args.putStringArray(Constants.TMDB_TRAILER_KEY, mArguments.getStringArray(Constants.TMDB_TRAILER_KEY));
        }
        return args;
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return mContext.getString(R.string.detail_general_tab);
            case 1:
                return mContext.getString(R.string.detail_cast_tab);
            case 2:
                return mContext.getString(R.string.detail_trailers_tab);
            case 3:
                return mContext.getString(R.string.detail_reviews_tab);
        }
        return null;
    }
}
