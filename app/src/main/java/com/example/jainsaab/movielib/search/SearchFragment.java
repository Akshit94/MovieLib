package com.example.jainsaab.movielib.search;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.TextView;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.example.jainsaab.movielib.R;
import com.example.jainsaab.movielib.movies.Movies;
import com.example.jainsaab.movielib.movies.MoviesAdapter;
import com.example.jainsaab.movielib.movies.MoviesGridFragment;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<Movies>>{
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String QUERY_STRING = "query_string";
    int mSectionNumber;
    MoviesAdapter searchMoviesAdapter;
    FloatingSearchView mFloatingSearchView;
    ViewPager mViewPager;
    TextView emptyText;

    public SearchFragment() {
    }

    public static SearchFragment newInstance(int sectionNumber) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);

        final Bundle bundle = getArguments();
        mSectionNumber = bundle.getInt(ARG_SECTION_NUMBER);

        ArrayList<Movies> searchMoviesArrayList = new ArrayList<>();
        GridView searchMoviesGridView = (GridView) rootView.findViewById(R.id.grid_view_search_movies);
        FrameLayout emptyView = (FrameLayout) rootView.findViewById(R.id.search_empty_view_box);
        searchMoviesGridView.setEmptyView(emptyView);
        searchMoviesAdapter = new MoviesAdapter(getActivity(), searchMoviesArrayList);
        searchMoviesGridView.setAdapter(searchMoviesAdapter);

        emptyText = (TextView) rootView.findViewById(R.id.empty_search_text);

        mViewPager = (ViewPager) getActivity().findViewById(R.id.container);
        mFloatingSearchView = (FloatingSearchView) getActivity().findViewById(R.id.floating_search_view);
        mFloatingSearchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, String newQuery) {
                mViewPager.setCurrentItem(3, true);
                mFloatingSearchView.showProgress();
                bundle.putString(QUERY_STRING, newQuery);
                getActivity().getSupportLoaderManager().initLoader(mSectionNumber, bundle, SearchFragment.this).forceLoad();
                getActivity().getSupportLoaderManager().restartLoader(mSectionNumber, bundle, SearchFragment.this).forceLoad();
                if(!MoviesGridFragment.isNetworkAvailable(getActivity())){
                    emptyText.setText(R.string.no_network_available);
                } else {
                    emptyText.setText(R.string.use_the_search_bar_to_search_for_movies);
                }
            }
        });

        return rootView;
    }

    @Override
    public Loader<List<Movies>> onCreateLoader(int id, Bundle args) {
        return new SearchMoviesLoader(getActivity(), id, args.getString(QUERY_STRING));
    }

    @Override
    public void onLoadFinished(Loader<List<Movies>> loader, List<Movies> data) {
        searchMoviesAdapter.setMovies(data);
        mFloatingSearchView.hideProgress();
    }

    @Override
    public void onLoaderReset(Loader<List<Movies>> loader) {
        searchMoviesAdapter.setMovies(new ArrayList<Movies>());
    }
}
