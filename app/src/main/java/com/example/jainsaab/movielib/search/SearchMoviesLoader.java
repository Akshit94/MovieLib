package com.example.jainsaab.movielib.search;

import android.content.Context;
import android.net.Uri;
import android.support.v4.content.AsyncTaskLoader;

import com.example.jainsaab.movielib.movies.Movies;
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

class SearchMoviesLoader extends AsyncTaskLoader<List<Movies>>{

    private int mSectionNumber;
    private String mQuery;

    SearchMoviesLoader(Context context, int sectionNumber, String query) {
        super(context);
        mSectionNumber = sectionNumber;
        mQuery = query;
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
        String jsonStr;

        try {
            if(mSectionNumber == 4){

                Uri builtUri = Uri.parse(Constants.SEARCH_BASE_URL).buildUpon()
                        .appendQueryParameter(Constants.API_KEY_PARAM, Constants.API_KEY)
                        .appendQueryParameter(Constants.TMDB_QUERY, mQuery)
                        .appendQueryParameter(Constants.TMDB_PAGE, "1")
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
                jsonStr = buffer.toString();

                return getMoviesDataFromJson(jsonStr);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}
