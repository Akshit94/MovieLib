package com.example.jainsaab.movielib.trailers;


import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.jainsaab.movielib.R;
import com.example.jainsaab.movielib.data.MoviesContract;
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


public class TrailerFragment extends Fragment {
    Bundle mArguments;
    View mRootView;

    public TrailerFragment(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_trailer, container, false);
        mArguments = getArguments();

        if(mArguments.containsKey(Constants.TMDB_GENRES_STRING)){
            showTrailers();
        } else {
            new FetchMovieTrailer(getActivity(), mRootView, mArguments).execute(1);
        }

        return mRootView;
    }

    private void showTrailers(){
        String trailerName[] = mArguments.getStringArray(Constants.TMDB_TRAILER_NAME);
        String trailerKey[] = mArguments.getStringArray(Constants.TMDB_TRAILER_KEY);
        if(trailerName != null){
            ArrayList<Trailer> trailerArrayList = new ArrayList<>(trailerName.length);
            for(int i = 0; i < trailerName.length; ++i){
                trailerArrayList.add(new Trailer(trailerName[i], trailerKey[i]));
            }
            if(trailerArrayList.size() != 0){
                ListView trailerList = (ListView) mRootView.findViewById(R.id.trailer_list_view);
                TextView emptyView = (TextView) mRootView.findViewById(R.id.trailer_empty_view);
                trailerList.setEmptyView(emptyView);
                ArrayList<Trailer> trailerArrayList1 = new ArrayList<>();
                TrailerAdapter trailerAdapter = new TrailerAdapter(getActivity(), trailerArrayList1);
                trailerList.setAdapter(trailerAdapter);
                trailerAdapter.setTrailer(trailerArrayList);
            }
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.menu_details, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public static class FetchMovieTrailer extends AsyncTask<Integer, Void, Integer> {
        private Context mContext;
        private View rootView;
        private Bundle arguments;

        public FetchMovieTrailer(Context context, View rootview1, Bundle args) {
            mContext = context;
            rootView = rootview1;
            arguments = args;
        }

        ArrayList<Trailer> trailerArrayList;

        private int getDataFromJson(String jsonStr, int integer) throws JSONException {

            JSONObject trailerJson = new JSONObject(jsonStr);
            JSONArray trailerArray = trailerJson.getJSONArray(Constants.TMDB_RESULTS);
            trailerArrayList = new ArrayList<>(trailerArray.length());
            for (int i = 0; i < trailerArray.length(); ++i) {
                JSONObject trailerObject = trailerArray.getJSONObject(i);
                trailerArrayList.add(new Trailer(trailerObject.getString(Constants.TMDB_TRAILER_NAME),
                        trailerObject.getString(Constants.TMDB_TRAILER_KEY)));
            }

            if(integer == 2){
                ContentValues trailerValues = new ContentValues();
                for(int i = 0; i < trailerArrayList.size(); ++i){
                    trailerValues.put(MoviesContract.TrailersEntry.MOVIE_ID, Integer.parseInt(arguments.getString(Constants.TMDB_MOVIE_ID)));
                    trailerValues.put(MoviesContract.TrailersEntry.TRAILER_NAME, trailerArrayList.get(i).getTrailerName());
                    trailerValues.put(MoviesContract.TrailersEntry.KEY, trailerArrayList.get(i).getTrailerKey());
                    mContext.getContentResolver().insert(MoviesContract.TrailersEntry.CONTENT_URI, trailerValues);
                }
            }

            return integer;
        }

        @Override
        protected Integer doInBackground(Integer... integers) {

            HttpURLConnection urlConnection;
            BufferedReader reader;
            InputStream inputStream;
            StringBuffer buffer = new StringBuffer();
            String line;
            String jsonStr;

            try {

                Uri builtUri = Uri.parse(Constants.TRAILER_REVIEWS_BASE_URL).buildUpon()
                        .appendPath(arguments.getString(Constants.TMDB_MOVIE_ID))
                        .appendPath(Constants.VIDEOS_PATH)
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
                    return 0;
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
                    return 0;
                }
                jsonStr = buffer.toString();

                return getDataFromJson(jsonStr, integers[0]);

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return 0;
        }

        @Override
            protected void onPostExecute(Integer integer) {

            if (trailerArrayList.size() != 0 && integer == 1) {
                ListView trailerList = (ListView) rootView.findViewById(R.id.trailer_list_view);
                TextView emptyView = (TextView) rootView.findViewById(R.id.trailer_empty_view);
                trailerList.setEmptyView(emptyView);
                ArrayList<Trailer> trailerArrayList1 = new ArrayList<>();
                TrailerAdapter trailerAdapter = new TrailerAdapter(mContext, trailerArrayList1);
                trailerList.setAdapter(trailerAdapter);
                trailerAdapter.setTrailer(trailerArrayList);
            }
            super.onPostExecute(integer);
        }
    }

}
