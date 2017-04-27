package com.example.jainsaab.movielib.pagerAdapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.jainsaab.movielib.R;
import com.example.jainsaab.movielib.favouriteMovies.FavouritesFragment;
import com.example.jainsaab.movielib.movies.MoviesGridFragment;
import com.example.jainsaab.movielib.search.SearchFragment;
import com.example.jainsaab.movielib.utility.Constants;

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {
    private Context mContext;

    public SectionsPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a MoviesGridFragment (defined as a static inner class below).
        int sectionNumber = position + 1;
        SharedPreferences getPrefs = PreferenceManager
                .getDefaultSharedPreferences(mContext);
        int popularMoviesTaskFlag = getPrefs.getInt(Constants.POPULAR_MOVIES_TASK_FLAG, 0);
        int upcomingMoviesTaskFlag = getPrefs.getInt(Constants.UPCOMING_MOVIES_TASK_FLAG, 0);
        int favouritesMoviesTaskFlag = getPrefs.getInt(Constants.FAVOURITE_MOVIES_TASK_FLAG, 0);

        if(sectionNumber == 1 && popularMoviesTaskFlag == 0){
            return MoviesGridFragment.newInstance(1);
        } else if(sectionNumber == 2 && upcomingMoviesTaskFlag == 0){
            return MoviesGridFragment.newInstance(2);
        } else if(sectionNumber == 3 && favouritesMoviesTaskFlag == 0){
            return FavouritesFragment.newInstance(3);
        } else if(sectionNumber == 4){
            return SearchFragment.newInstance(4);
        } else {
            return null;
        }
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return mContext.getString(R.string.popular_tab);
            case 1:
                return mContext.getString(R.string.upcoming_tab);
            case 2:
                return mContext.getString(R.string.favourites_tab);
            case 3:
                return mContext.getString(R.string.search_tab);
        }
        return null;
    }
}
