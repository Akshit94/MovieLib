package com.example.jainsaab.movielib.cast;

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

public class CastFragment extends Fragment {
    Bundle mArguments;
    View mRootView;

    public CastFragment(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_cast, container, false);
        mArguments = getArguments();

        if(mArguments.containsKey(Constants.TMDB_GENRES_STRING)){
            showCast();
        } else {
            new FetchMovieCast(getActivity(), mRootView, mArguments).execute(1);
        }

        return mRootView;
    }

    private void showCast(){
        String[] character = mArguments.getStringArray(Constants.TMDB_CHARACTER_NAME);
        String[] actor = mArguments.getStringArray(Constants.TMDB_ACTOR_NAME_ARGUMENT);
        if(actor != null){
            ArrayList<Cast> castArrayList = new ArrayList<>(actor.length);
            for(int i = 0; i < actor.length; ++i){
                castArrayList.add(new Cast(actor[i], character[i], null));
            }
            ListView castList = (ListView) mRootView.findViewById(R.id.person_list_view);
            TextView emptyView = (TextView) mRootView.findViewById(R.id.cast_empty_view);
            castList.setEmptyView(emptyView);
            ArrayList<Cast> castArrayList1 = new ArrayList<>();
            CastAdapter castAdapter = new CastAdapter(getActivity(), castArrayList1);
            castList.setAdapter(castAdapter);
            castAdapter.setCast(castArrayList);
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

    public static class FetchMovieCast extends AsyncTask<Integer, Void, Integer> {
        private Context mContext;
        private View rootView;
        private Bundle arguments;

        public FetchMovieCast(Context context, View rootview1, Bundle args) {
            mContext = context;
            rootView = rootview1;
            arguments = args;
        }

        ArrayList<Cast> castArrayList;

        private int getDataFromJson(String jsonStr, int integer) throws JSONException {

            JSONObject castJson = new JSONObject(jsonStr);
            JSONArray castArray = castJson.getJSONArray(Constants.TMDB_CAST);
            castArrayList = new ArrayList<>(castArray.length());
            for (int i = 0; i < castArray.length(); ++i) {
                JSONObject castObject = castArray.getJSONObject(i);
                castArrayList.add(new Cast(castObject.getString(Constants.TMDB_ACTOR_NAME),
                        castObject.getString(Constants.TMDB_CHARACTER_NAME),
                        castObject.getString(Constants.TMDB_PROFILE_PATH)));
            }

            if(integer == 2){
                ContentValues castValues = new ContentValues();
                for(int i = 0; i < castArrayList.size(); ++i){
                    castValues.put(MoviesContract.CastEntry.MOVIE_ID, Integer.parseInt(arguments.getString(Constants.TMDB_MOVIE_ID)));
                    castValues.put(MoviesContract.CastEntry.CHARACTER_NAME, castArrayList.get(i).getCharacterName());
                    castValues.put(MoviesContract.CastEntry.ACTOR_NAME, castArrayList.get(i).getActorName());
                    mContext.getContentResolver().insert(MoviesContract.CastEntry.CONTENT_URI, castValues);
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

                Uri builtUri = Uri.parse(Constants.CAST_BASE_URL).buildUpon()
                        .appendPath(arguments.getString(Constants.TMDB_MOVIE_ID))
                        .appendPath(Constants.CREDITS_PATH)
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

            if (castArrayList != null && castArrayList.size() != 0 && integer == 1) {
                ListView castList = (ListView) rootView.findViewById(R.id.person_list_view);
                TextView emptyView = (TextView) rootView.findViewById(R.id.cast_empty_view);
                castList.setEmptyView(emptyView);
                ArrayList<Cast> castArrayList1 = new ArrayList<>();
                CastAdapter castAdapter = new CastAdapter(mContext, castArrayList1);
                castList.setAdapter(castAdapter);
                castAdapter.setCast(castArrayList);
            }
            super.onPostExecute(integer);
        }
    }

}
