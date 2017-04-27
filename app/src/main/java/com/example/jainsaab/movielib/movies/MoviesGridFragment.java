package com.example.jainsaab.movielib.movies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.example.jainsaab.movielib.R;
import com.example.jainsaab.movielib.SignInActivity;
import com.example.jainsaab.movielib.utility.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MoviesGridFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<Movies>> {
    MoviesAdapter moviesAdapter;
    int mSectionNumber;
    ProgressBar emptyProgressBar;
    TextView emptyTextView;
    ImageView emptyImageView;
    FloatingSearchView mFloatingSearchView;
    ViewPager mViewPager;
    SharedPreferences getPrefs;


    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    public MoviesGridFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static MoviesGridFragment newInstance(int sectionNumber) {
        MoviesGridFragment fragment = new MoviesGridFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movies, container, false);

        Bundle bundle = getArguments();
        mSectionNumber = bundle.getInt(ARG_SECTION_NUMBER);

        ArrayList<Movies> moviesArrayList = new ArrayList<>();
        GridView moviesGridView = (GridView) rootView.findViewById(R.id.grid_view_movies);
        LinearLayout emptyView = (LinearLayout) rootView.findViewById(R.id.empty_view_box);
        emptyProgressBar = (ProgressBar) rootView.findViewById(R.id.empty_progress_bar);
        emptyTextView = (TextView) rootView.findViewById(R.id.empty_text_view);
        emptyImageView = (ImageView) rootView.findViewById(R.id.empty_image_view);
        moviesGridView.setEmptyView(emptyView);
        moviesAdapter = new MoviesAdapter(getActivity(), moviesArrayList);
        moviesGridView.setAdapter(moviesAdapter);

        mViewPager = (ViewPager) getActivity().findViewById(R.id.container);
        mFloatingSearchView = (FloatingSearchView) getActivity().findViewById(R.id.floating_search_view);
        mFloatingSearchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, String newQuery) {
                mViewPager.setCurrentItem(3, true);
                mFloatingSearchView.showProgress();
            }
        });

        getPrefs = PreferenceManager
                .getDefaultSharedPreferences(getActivity());

        mFloatingSearchView.setOnMenuItemClickListener(new FloatingSearchView.OnMenuItemClickListener() {
            @Override
            public void onActionMenuItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.sign_out_menu:
                        SharedPreferences.Editor e = getPrefs.edit();
                        e.putString(Constants.SESSION_SHARED_PREFS, null);
                        e.apply();
                        startActivity(new Intent(getActivity(), SignInActivity.class));
                        getActivity().finish();
                }
            }
        });

        getActivity().getSupportLoaderManager().initLoader(mSectionNumber, bundle, this).forceLoad();

        return rootView;
    }

    @Override
    public Loader<List<Movies>> onCreateLoader(int id, Bundle args) {
        return new MoviesLoader(getActivity(), id);
    }


    @Override
    public void onLoadFinished(Loader<List<Movies>> loader, List<Movies> data) {
        moviesAdapter.setMovies(data);
        emptyProgressBar.setVisibility(View.GONE);
        if (!isNetworkAvailable(getActivity())) {
            updateEmptyView(R.string.no_network_available);
        } else {
            SharedPreferences getPrefs = PreferenceManager
                    .getDefaultSharedPreferences(getActivity());
            String status = getPrefs.getString(Constants.SERVER_STATUS_FLAG, Constants.SERVER_STATUS_OK);
            if (status.equals(Constants.SERVER_STATUS_DOWN)) {
                updateEmptyView(R.string.server_down);
            } else if (status.equals(Constants.SERVER_STATUS_INVALID)) {
                updateEmptyView(R.string.server_invalid);
            }
        }
    }

    private void updateEmptyView(int stringId) {
        emptyTextView.setText(stringId);
        emptyImageView.setImageDrawable(getResources().getDrawable(R.drawable.no_internet));
    }

    @Override
    public void onLoaderReset(Loader<List<Movies>> loader) {
        moviesAdapter.setMovies(new ArrayList<Movies>());
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
}
