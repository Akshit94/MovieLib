package com.example.jainsaab.movielib.movies;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.content.AsyncTaskLoader;

import com.example.jainsaab.movielib.utility.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

class MoviesLoader extends AsyncTaskLoader<List<Movies>>{
    private Context mContext;
    private int mSectionNumber;

    MoviesLoader(Context context, int sectionNumber) {
        super(context);
        mContext = context;
        mSectionNumber = sectionNumber;
    }

    private ArrayList<Movies> getMoviesDataFromJson(String moviesJsonStr)
            throws JSONException {

        JSONObject moviesJson = new JSONObject(moviesJsonStr);
        JSONArray moviesArray = moviesJson.getJSONArray(Constants.TMDB_RESULTS);
        ArrayList<Movies> moviesArrayList = new ArrayList<>(moviesArray.length());


        for (int i = 0; i < moviesArray.length(); ++i) {
            JSONObject movieObject = moviesArray.getJSONObject(i);
            String cbfcRating;
            if(!movieObject.getBoolean(Constants.TMDB_ADULT)){
                cbfcRating = "U/A";
            } else {
                cbfcRating = "A";
            }

            JSONArray genreArray = movieObject.getJSONArray(Constants.TMDB_GENRES);
            int[] genre = new int[genreArray.length()];
            for (int j = 0; j < genreArray.length(); ++j) {
                genre[j] = genreArray.getInt(j);
            }

            moviesArrayList.add(new Movies(movieObject.getString(Constants.TMDB_TITLE),
                    movieObject.getString(Constants.TMDB_RELEASE_DATE),
                    movieObject.getString(Constants.TMDB_POSTER),
                    cbfcRating,
                    movieObject.getString(Constants.TMDB_BACKDROP),
                    movieObject.getString(Constants.TMDB_VOTE_AVG),
                    genre,
                    movieObject.getString(Constants.TMDB_OVERVIEW),
                    movieObject.getString(Constants.TMDB_MOVIE_ID)));
        }
        return moviesArrayList;
    }

    @Override
    public List<Movies> loadInBackground() {

        HttpURLConnection urlConnection;
        BufferedReader reader;
        InputStream inputStream;
        StringBuffer buffer = new StringBuffer();
        String line;
        String moviesJsonStr;

        SharedPreferences getPrefs = PreferenceManager
                .getDefaultSharedPreferences(mContext);
        try {
            if (mSectionNumber == 1) {

                Uri builtUri = Uri.parse(Constants.POPULAR_MOVIES_BASE_URL).buildUpon()
                        .appendQueryParameter(Constants.SORT_PARAM, Constants.POPULARITY_SORT)
                        .appendQueryParameter(Constants.VOTE_COUNT_PARAM, Constants.VOTE_COUNT)
                        .appendQueryParameter(Constants.API_KEY_PARAM, Constants.API_KEY)
                        .build();
                URL url = new URL(builtUri.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                inputStream = urlConnection.getInputStream();

                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line).append("\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                moviesJsonStr = buffer.toString();

                SharedPreferences.Editor e = getPrefs.edit();
                e.putInt(Constants.POPULAR_MOVIES_TASK_FLAG, 1);
                e.apply();

                return getMoviesDataFromJson(moviesJsonStr);

            } else if (mSectionNumber == 2) {

                Uri builtUri = Uri.parse(Constants.UPCOMING_MOVIES_BASE_URL).buildUpon()
                        .appendQueryParameter(Constants.API_KEY_PARAM, Constants.API_KEY)
                        .build();
                URL url = new URL(builtUri.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                inputStream = urlConnection.getInputStream();

                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line).append("\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                moviesJsonStr = buffer.toString();

                SharedPreferences.Editor e = getPrefs.edit();
                e.putInt(Constants.UPCOMING_MOVIES_TASK_FLAG, 1);
                e.apply();

                return getMoviesDataFromJson(moviesJsonStr);

            }
        } catch (IOException e) {
            SharedPreferences.Editor prefsEdit = getPrefs.edit();
            prefsEdit.putString(Constants.SERVER_STATUS_FLAG,Constants.SERVER_STATUS_DOWN);
            prefsEdit.apply();
            e.printStackTrace();
        } catch (JSONException e) {
            SharedPreferences.Editor prefsEdit = getPrefs.edit();
            prefsEdit.putString(Constants.SERVER_STATUS_FLAG,Constants.SERVER_STATUS_INVALID);
            prefsEdit.apply();
            e.printStackTrace();
        }

        return null;

    }

}
