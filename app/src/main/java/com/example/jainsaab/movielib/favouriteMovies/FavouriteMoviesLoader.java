package com.example.jainsaab.movielib.favouriteMovies;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.support.v4.content.AsyncTaskLoader;

import com.example.jainsaab.movielib.data.MoviesContract;
import com.example.jainsaab.movielib.utility.Constants;

import java.util.ArrayList;
import java.util.List;

class FavouriteMoviesLoader extends AsyncTaskLoader<List<FavouriteMovies>>{

    private Context mContext;
    private int mSectionNumber;

    FavouriteMoviesLoader(Context context, int sectionNumber) {
        super(context);
        mContext = context;
        mSectionNumber = sectionNumber;
    }

    @Override
    public List<FavouriteMovies> loadInBackground() {

        SharedPreferences getPrefs = PreferenceManager
                .getDefaultSharedPreferences(mContext);

        if (mSectionNumber == 3) {

            Cursor cursor = mContext.getContentResolver().query(MoviesContract.MoviesEntry.CONTENT_URI, null, null, null, null);
            ArrayList<FavouriteMovies> favouriteMoviesArrayList = null;
            int i = 0;
            if (cursor.getCount() != 0) {
                favouriteMoviesArrayList = new ArrayList<>(cursor.getCount());
                cursor.moveToFirst();
                do {

                    Cursor reviewCursor = mContext.getContentResolver().query(
                            MoviesContract.ReviewsEntry.buildMovieReviewsUri(cursor.getInt(cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_MOVIE_ID))),
                            null, null, null, null);
                    String authorName[] = null, content[] = null, reviewUrl[] = null;

                    if(reviewCursor.getCount() != 0){
                        authorName = new String[reviewCursor.getCount()];
                        content = new String[reviewCursor.getCount()];
                        reviewUrl = new String[reviewCursor.getCount()];
                        int j = 0;
                        while (reviewCursor.moveToNext() && j < reviewCursor.getCount()){
                            authorName[j] = reviewCursor.getString(reviewCursor.getColumnIndex(MoviesContract.ReviewsEntry.AUTHOR_NAME));
                            content[j] = reviewCursor.getString(reviewCursor.getColumnIndex(MoviesContract.ReviewsEntry.CONTENT));
                            reviewUrl[j] = reviewCursor.getString(reviewCursor.getColumnIndex(MoviesContract.ReviewsEntry.REVIEW_URL));
                            ++j;
                        }
                    }

                    Cursor trailerCursor = mContext.getContentResolver().query(
                            MoviesContract.TrailersEntry.buildMovieTrailerUri(cursor.getInt(cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_MOVIE_ID))),
                            null, null, null, null);
                    String trailerName[] = null, key[] = null;

                    if(trailerCursor.getCount() != 0){
                        trailerName = new String[trailerCursor.getCount()];
                        key = new String[trailerCursor.getCount()];
                        int k = 0;
                        while (trailerCursor.moveToNext()) {
                            trailerName[k] = trailerCursor.getString(trailerCursor.getColumnIndex(MoviesContract.TrailersEntry.TRAILER_NAME));
                            key[k] = trailerCursor.getString(trailerCursor.getColumnIndex(MoviesContract.TrailersEntry.KEY));
                            ++k;
                        }
                    }

                    Cursor castCursor = mContext.getContentResolver().query(
                            MoviesContract.CastEntry.buildMovieCastUri(cursor.getInt(cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_MOVIE_ID))),
                            null, null, null, null);
                    String characterName[] = null, actorName[] = null;

                    if(castCursor.getCount() != 0){
                        characterName = new String[castCursor.getCount()];
                        actorName = new String[castCursor.getCount()];
                        int l = 0;
                        while (castCursor.moveToNext()) {
                            characterName[l] = castCursor.getString(castCursor.getColumnIndex(MoviesContract.CastEntry.CHARACTER_NAME));
                            actorName[l] = castCursor.getString(castCursor.getColumnIndex(MoviesContract.CastEntry.ACTOR_NAME));
                            ++l;
                        }
                    }

                    favouriteMoviesArrayList.add(new FavouriteMovies(
                                    cursor.getInt(cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_MOVIE_ID)),
                                    cursor.getString(cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_TITLE)),
                                    cursor.getString(cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_RELEASE_DATE)),
                                    cursor.getString(cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_USER_RATING)),
                                    cursor.getString(cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_GENRE)),
                                    cursor.getString(cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_PLOT)),
                                    cursor.getString(cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_CBFC_RATING)),
                                    authorName,
                                    content,
                                    reviewUrl,
                                    characterName,
                                    actorName,
                                    trailerName,
                                    key));
                    ++i;
                    castCursor.close();
                    trailerCursor.close();
                    reviewCursor.close();
                } while (cursor.moveToNext() && i < cursor.getCount());
                cursor.close();
            }

            SharedPreferences.Editor e = getPrefs.edit();
            e.putInt(Constants.FAVOURITE_MOVIES_TASK_FLAG, 1);
            e.apply();

            return favouriteMoviesArrayList;

        }

        return null;
    }
}
