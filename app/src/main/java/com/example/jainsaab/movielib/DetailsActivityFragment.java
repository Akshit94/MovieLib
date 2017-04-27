package com.example.jainsaab.movielib;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jainsaab.movielib.utility.Constants;
import com.example.jainsaab.movielib.utility.Genre;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailsActivityFragment extends Fragment {
    View mRootView;
    Bundle mArguments;
    ImageView mBackdropImage, mPosterImage;
    TextView mReleaseDate, mUserRating, mGenre, mPlot;
    FloatingActionButton mFavFab;
    SharedPreferences getPrefs;
    ShareActionProvider mShareActionProvider;

    public DetailsActivityFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_details, container, false);
        mArguments = getArguments();

        mBackdropImage = (ImageView) getActivity().findViewById(R.id.detail_backdrop);
        mFavFab = (FloatingActionButton) getActivity().findViewById(R.id.favourite_fab);
        mPosterImage = (ImageView) mRootView.findViewById(R.id.detail_poster);
        mReleaseDate = (TextView) mRootView.findViewById(R.id.release_date);
        mUserRating = (TextView) mRootView.findViewById(R.id.user_rating);
        mGenre = (TextView) mRootView.findViewById(R.id.genre);
        mPlot = (TextView) mRootView.findViewById(R.id.overview_text_view);
        getPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

        getActivity().setTitle(mArguments.getString(Constants.TMDB_TITLE));

        mFavFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getPrefs.getInt(setMovieId(), 1) == 1) {
                    mArguments.putByteArray(Constants.TMDB_POSTER_BYTE_ARRAY, convertImageViewToByteArray(mPosterImage));
                    mArguments.putByteArray(Constants.TMDB_BACKDROP_BYTE_ARRAY, convertImageViewToByteArray(mBackdropImage));
                    mArguments.putString(Constants.TMDB_GENRES_STRING, mGenre.getText().toString());
                    mFavFab.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_white_24dp));
                } else {
                    mFavFab.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_border_white_24dp));
                }
                new AddFavouriteTask(getActivity(), mRootView, mArguments).execute();
            }
        });

        if (mArguments.containsKey(Constants.TMDB_ACTOR_NAME_ARGUMENT)) {
            showingFavouriteDetails();
        } else {
            showingDetails();
        }

        return mRootView;
    }

    private byte[] convertImageViewToByteArray(ImageView imageView) {
        imageView.setDrawingCacheEnabled(true);
        imageView.buildDrawingCache();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Bitmap bm = imageView.getDrawingCache();
        byte[] byteArray;
        if (bm != null) {
            bm.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byteArray = stream.toByteArray();
        } else {
            byteArray = null;
        }
        return byteArray;
    }

    private String setMovieId() {
        String movieId;
        if (mArguments.containsKey(Constants.TMDB_ACTOR_NAME_ARGUMENT)) {
            movieId = "" + mArguments.getInt(Constants.TMDB_MOVIE_ID);
        } else {
            movieId = mArguments.getString(Constants.TMDB_MOVIE_ID);
        }
        return movieId;
    }

    private void setFabIcon() {
        String movieId;
        movieId = setMovieId();

        if (getPrefs.getInt(movieId, 1) == 1) {
            mFavFab.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_border_white_24dp));
        } else {
            mFavFab.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_white_24dp));
        }
    }

    private void showingFavouriteDetails() {

        setFabIcon();

        String backdropStr = getPrefs.getString(mArguments.getInt(Constants.TMDB_MOVIE_ID) + "backdrop", null);
        if (backdropStr != null) {
            byte[] backdropByteArray = Base64.decode(backdropStr, Base64.DEFAULT);
            mBackdropImage.setImageBitmap(BitmapFactory.decodeByteArray(backdropByteArray, 0, backdropByteArray.length));
        }

        String posterStr = getPrefs.getString(mArguments.getInt(Constants.TMDB_MOVIE_ID) + "poster", null);
        if (posterStr != null) {
            byte[] posterByteArray = Base64.decode(posterStr, Base64.DEFAULT);
            mPosterImage.setImageBitmap(BitmapFactory.decodeByteArray(posterByteArray, 0, posterByteArray.length));
        }

        mReleaseDate.setText(mArguments.getString(Constants.TMDB_RELEASE_DATE));

        if (Double.parseDouble(mArguments.getString(Constants.TMDB_VOTE_AVG)) == 0) {
            mUserRating.setText(R.string.not_yet_rated);
            mUserRating.setTypeface(mUserRating.getTypeface(), Typeface.ITALIC);
        } else {
            mUserRating.setText(mArguments.getString(Constants.TMDB_VOTE_AVG));
        }

        mGenre.setText(mArguments.getString(Constants.TMDB_GENRES_STRING));

        mPlot.setText(mArguments.getString(Constants.TMDB_OVERVIEW));

    }

    private void showingDetails() {

        setFabIcon();

        final String BACKDROP_FINAL_URL = Constants.IMAGE_BASE_URL +
                Constants.POSTER_SIZE_LARGE + mArguments.getString(Constants.TMDB_BACKDROP);
        Picasso.with(getActivity()).load(BACKDROP_FINAL_URL.trim()).into(mBackdropImage);

        final String POSTER_FINAL_URL = Constants.IMAGE_BASE_URL +
                Constants.POSTER_SIZE_LARGE + mArguments.getString(Constants.TMDB_POSTER);
        Picasso.with(getActivity()).load(POSTER_FINAL_URL.trim()).into(mPosterImage);

        mReleaseDate.setText(mArguments.getString(Constants.TMDB_RELEASE_DATE));

        if (Double.parseDouble(mArguments.getString(Constants.TMDB_VOTE_AVG)) == 0) {
            mUserRating.setText(R.string.not_yet_rated);
            mUserRating.setTypeface(mUserRating.getTypeface(), Typeface.ITALIC);
        } else {
            mUserRating.setText(mArguments.getString(Constants.TMDB_VOTE_AVG));
        }

        int[] genre = mArguments.getIntArray(Constants.TMDB_GENRES);
        String genreName;
        Genre genreObject = new Genre();
        for (int i = 0; i < genre.length; ++i) {
            genreName = genreObject.getGenreName(genre[i], getActivity());
            mGenre.append(genreName);
            if (i < genre.length - 1) {
                mGenre.append(", ");
            }
        }

        mPlot.setText(mArguments.getString(Constants.TMDB_OVERVIEW));

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.menu_details, menu);
        MenuItem menuItem = menu.findItem(R.id.action_detail_share);
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
        if (mShareActionProvider != null)
            mShareActionProvider.setShareIntent(createShareMoviesIntent());
        else
            Log.d("DetailActivity", "ShareActionProvide is null");
        super.onCreateOptionsMenu(menu, inflater);
    }

    private Intent createShareMoviesIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, mArguments.getString(Constants.TMDB_TITLE)
                + "\n" + getString(R.string.share_release) + mArguments.getString(Constants.TMDB_RELEASE_DATE)
                + "\n" + getString(R.string.share_user_rating) + mArguments.getString(Constants.TMDB_VOTE_AVG)
                + "\n" + getString(R.string.share_cbfc_rating) + mArguments.getString(Constants.TMDB_ADULT)
                + "\n" + getString(R.string.share_plot) + "\n" + mArguments.get(Constants.TMDB_OVERVIEW));
        return shareIntent;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
