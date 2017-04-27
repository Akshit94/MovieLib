package com.example.jainsaab.movielib.favouriteMovies;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridView;

import com.example.jainsaab.movielib.R;

import java.util.ArrayList;
import java.util.List;

public class FavouritesFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<FavouriteMovies>>{
    FavouriteMoviesAdapter favouriteMoviesAdapter;
    int mSectionNumber;
    private static final String ARG_SECTION_NUMBER = "section_number";
    public FavouritesFragment() {
    }

    public static FavouritesFragment newInstance(int sectionNumber) {
        FavouritesFragment fragment = new FavouritesFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_favourites, container, false);

        Bundle bundle = getArguments();
        mSectionNumber = bundle.getInt(ARG_SECTION_NUMBER);

        ArrayList<FavouriteMovies> favouriteMoviesArrayList = new ArrayList<>();
        GridView favouriteMoviesGridView = (GridView) rootView.findViewById(R.id.grid_view_favourite_movies);
        FrameLayout emptyView = (FrameLayout) rootView.findViewById(R.id.favourite_empty_view_box);
        favouriteMoviesGridView.setEmptyView(emptyView);
        favouriteMoviesAdapter = new FavouriteMoviesAdapter(getActivity(), favouriteMoviesArrayList);
        favouriteMoviesGridView.setAdapter(favouriteMoviesAdapter);

        getActivity().getSupportLoaderManager().initLoader(mSectionNumber, bundle, this).forceLoad();

        return rootView;
    }

    @Override
    public Loader<List<FavouriteMovies>> onCreateLoader(int id, Bundle args) {
        return new FavouriteMoviesLoader(getActivity(), id);
    }

    @Override
    public void onLoadFinished(Loader<List<FavouriteMovies>> loader, List<FavouriteMovies> data) {
        favouriteMoviesAdapter.setFavouriteMovies(data);
    }

    @Override
    public void onLoaderReset(Loader<List<FavouriteMovies>> loader) {
        favouriteMoviesAdapter.setFavouriteMovies(new ArrayList<FavouriteMovies>());
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().getSupportLoaderManager().restartLoader(3, null, this).forceLoad();
    }
}
