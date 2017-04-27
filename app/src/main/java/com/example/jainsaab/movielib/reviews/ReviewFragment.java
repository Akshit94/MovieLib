package com.example.jainsaab.movielib.reviews;

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

public class ReviewFragment extends Fragment {

    Bundle mArguments;
    View mRootView;

    public ReviewFragment(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_review, container, false);
        mArguments = getArguments();

        if(mArguments.containsKey(Constants.TMDB_GENRES_STRING)){
            showReviews();
        } else {
            new FetchMovieReviews(getActivity(), mRootView, mArguments).execute(1);
        }

        return mRootView;
    }

    private void showReviews(){
        String author[] = mArguments.getStringArray(Constants.TMDB_AUTHOR);
        String content[] = mArguments.getStringArray(Constants.TMDB_CONTENT);
        String reviewUrl[] = mArguments.getStringArray(Constants.TMDB_REVIEW_URL);
        if(author != null){
            ArrayList<Review> reviewArrayList = new ArrayList<>(author.length);
            for(int i = 0; i < author.length; ++i){
                reviewArrayList.add(new Review(author[i], content[i], reviewUrl[i]));
            }
            if (reviewArrayList.size() != 0) {
                ListView reviewList = (ListView) mRootView.findViewById(R.id.review_list_view);
                TextView emptyView = (TextView) mRootView.findViewById(R.id.review_empty_view);
                reviewList.setEmptyView(emptyView);
                ArrayList<Review> reviewArrayList1 = new ArrayList<>();
                ReviewAdapter reviewAdapter = new ReviewAdapter(getActivity(), reviewArrayList1);
                reviewList.setAdapter(reviewAdapter);
                reviewAdapter.setReview(reviewArrayList);
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

    public static class FetchMovieReviews extends AsyncTask<Integer, Void, Integer> {
        private Context mContext;
        private View rootView;
        private Bundle arguments;

        public FetchMovieReviews(Context context, View rootview1, Bundle args) {
            mContext = context;
            rootView = rootview1;
            arguments = args;
        }

        ArrayList<Review> reviewsArrayList;

        private int getDataFromJson(String jsonStr, int integer) throws JSONException {

            JSONObject reviewJson = new JSONObject(jsonStr);
            JSONArray reviewArray = reviewJson.getJSONArray(Constants.TMDB_RESULTS);
            reviewsArrayList = new ArrayList<>(reviewArray.length());
            for (int i = 0; i < reviewArray.length(); ++i) {
                JSONObject reviewObject = reviewArray.getJSONObject(i);
                reviewsArrayList.add(new Review(reviewObject.getString(Constants.TMDB_AUTHOR),
                        reviewObject.getString(Constants.TMDB_CONTENT),
                        reviewObject.getString(Constants.TMDB_REVIEW_URL)));
            }

            if(integer == 2){
                ContentValues reviewValues = new ContentValues();
                for(int i = 0; i < reviewsArrayList.size(); ++i){
                    reviewValues.put(MoviesContract.ReviewsEntry.MOVIE_ID, Integer.parseInt(arguments.getString(Constants.TMDB_MOVIE_ID)));
                    reviewValues.put(MoviesContract.ReviewsEntry.AUTHOR_NAME, reviewsArrayList.get(i).getAuthorName());
                    reviewValues.put(MoviesContract.ReviewsEntry.CONTENT, reviewsArrayList.get(i).getReviewContent());
                    reviewValues.put(MoviesContract.ReviewsEntry.REVIEW_URL, reviewsArrayList.get(i).getReviewUrl());
                    mContext.getContentResolver().insert(MoviesContract.ReviewsEntry.CONTENT_URI, reviewValues);
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
                        .appendPath(Constants.REVIEWS_PATH)
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

            if (reviewsArrayList.size() != 0 && integer == 1) {
                ListView reviewList = (ListView) rootView.findViewById(R.id.review_list_view);
                TextView emptyView = (TextView) rootView.findViewById(R.id.review_empty_view);
                reviewList.setEmptyView(emptyView);
                ArrayList<Review> reviewArrayList1 = new ArrayList<>();
                ReviewAdapter reviewAdapter = new ReviewAdapter(mContext, reviewArrayList1);
                reviewList.setAdapter(reviewAdapter);
                reviewAdapter.setReview(reviewsArrayList);
            }
            super.onPostExecute(integer);
        }
    }

}
