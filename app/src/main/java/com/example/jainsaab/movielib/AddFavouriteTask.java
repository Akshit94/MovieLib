package com.example.jainsaab.movielib;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.view.View;
import android.widget.Toast;

import com.example.jainsaab.movielib.cast.CastFragment;
import com.example.jainsaab.movielib.data.MoviesContract;
import com.example.jainsaab.movielib.reviews.ReviewFragment;
import com.example.jainsaab.movielib.trailers.TrailerFragment;
import com.example.jainsaab.movielib.utility.Constants;

public class AddFavouriteTask extends AsyncTask<Void, Void, ContentValues>{
    private Context mContext;
    private View mRootView;
    private Bundle mArguments;
    private ProgressDialog dialog;
    public static final String ACTION_DATA_UPDATED = "com.example.jainsaab.movielib.ACTION_DATA_UPDATED";


    AddFavouriteTask(Context context, View rootView, Bundle arguments){
        mContext = context;
        mRootView = rootView;
        mArguments = arguments;
    }

    @Override
    protected void onPreExecute() {
        dialog = new ProgressDialog(mContext);
        SharedPreferences getPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        int insertFlag = getPrefs.getInt(getMovieId(),1);
        if(insertFlag == 1){
            dialog.setMessage(mContext.getString(R.string.adding_to_favourites_dilog));
        } else {
            dialog.setMessage(mContext.getString(R.string.removing_from_favourites_dilog));
        }
        dialog.setCancelable(false);
        dialog.show();
    }

    private String getMovieId(){
        String movieId;
        if (mArguments.containsKey(Constants.TMDB_ACTOR_NAME_ARGUMENT)) {
            movieId = "" + mArguments.getInt(Constants.TMDB_MOVIE_ID);
        } else {
            movieId = mArguments.getString(Constants.TMDB_MOVIE_ID);
        }
        return movieId;
    }

    @Override
    protected ContentValues doInBackground(Void... voids) {

        String movieId = getMovieId();

        SharedPreferences getPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        int insertFlag = getPrefs.getInt(movieId,1);
        ContentValues movieValues = new ContentValues();
        if(insertFlag == 1){
            movieValues.put(MoviesContract.MoviesEntry.COLUMN_MOVIE_ID, Integer.parseInt(movieId));
            movieValues.put(MoviesContract.MoviesEntry.COLUMN_TITLE,  mArguments.getString(Constants.TMDB_TITLE));
            movieValues.put(MoviesContract.MoviesEntry.COLUMN_RELEASE_DATE,  mArguments.getString(Constants.TMDB_RELEASE_DATE));
            movieValues.put(MoviesContract.MoviesEntry.COLUMN_USER_RATING,  mArguments.getString(Constants.TMDB_VOTE_AVG));
            movieValues.put(MoviesContract.MoviesEntry.COLUMN_GENRE,  mArguments.getString(Constants.TMDB_GENRES_STRING));
            movieValues.put(MoviesContract.MoviesEntry.COLUMN_PLOT,  mArguments.getString(Constants.TMDB_OVERVIEW));
            movieValues.put(MoviesContract.MoviesEntry.COLUMN_CBFC_RATING,  mArguments.getString(Constants.TMDB_ADULT));
            mContext.getContentResolver().insert(
                    MoviesContract.MoviesEntry.CONTENT_URI,
                    movieValues);
            SharedPreferences.Editor e = getPrefs.edit();
            e.putInt(movieId, 0);
            String poster = Base64.encodeToString(mArguments.getByteArray(Constants.TMDB_POSTER_BYTE_ARRAY), Base64.DEFAULT);
            e.putString(movieId + "poster", poster);
            String backdrop = Base64.encodeToString(mArguments.getByteArray(Constants.TMDB_BACKDROP_BYTE_ARRAY), Base64.DEFAULT);
            e.putString(movieId + "backdrop", backdrop);
            e.apply();
        } else {
            movieValues = null;
            mContext.getContentResolver().delete(MoviesContract.MoviesEntry.CONTENT_URI,
                    MoviesContract.MoviesEntry.COLUMN_MOVIE_ID + " = ?",
                    new String[]{movieId});

            mContext.getContentResolver().delete(MoviesContract.ReviewsEntry.CONTENT_URI,
                    MoviesContract.ReviewsEntry.MOVIE_ID + " = ?",
                    new String[]{movieId});

            mContext.getContentResolver().delete(MoviesContract.TrailersEntry.CONTENT_URI,
                    MoviesContract.TrailersEntry.MOVIE_ID + " = ?",
                    new String[]{movieId});

            mContext.getContentResolver().delete(MoviesContract.CastEntry.CONTENT_URI,
                    MoviesContract.CastEntry.MOVIE_ID + " = ?",
                    new String[]{movieId});

            SharedPreferences.Editor e = getPrefs.edit();
            e.putInt(movieId, 1);
            e.putString(movieId + "poster", null);
            e.putString(movieId + "backdrop", null);
            e.apply();
            if (mArguments.containsKey(Constants.TMDB_ACTOR_NAME_ARGUMENT)) {
                Activity activity = (Activity) mContext;
                activity.finish();
            }
        }

        return movieValues;

    }

    private void updateWidget(){
        Intent dataUpdatedIntent = new Intent(ACTION_DATA_UPDATED).setPackage(mContext.getPackageName());
        mContext.sendBroadcast(dataUpdatedIntent);
    }

    @Override
    protected void onPostExecute(ContentValues contentValues) {

        if (contentValues != null) {
            new CastFragment.FetchMovieCast(mContext, mRootView, mArguments).execute(2);
            new TrailerFragment.FetchMovieTrailer(mContext, mRootView, mArguments).execute(2);
            new ReviewFragment.FetchMovieReviews(mContext, mRootView, mArguments).execute(2);
            Toast.makeText(mContext, R.string.favourites_added, Toast.LENGTH_SHORT).show();
        } else{
            Toast.makeText(mContext, R.string.favourites_removed, Toast.LENGTH_SHORT).show();
        }

        if (dialog.isShowing()) {
            dialog.dismiss();
        }

        updateWidget();

        super.onPostExecute(contentValues);
    }
}
